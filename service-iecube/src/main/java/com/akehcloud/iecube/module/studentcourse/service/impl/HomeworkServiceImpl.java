package com.akehcloud.iecube.module.studentcourse.service.impl;

import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.module.lessonschedule.entity.LessonScheduleDO;
import com.akehcloud.iecube.module.lessonschedule.mapper.LessonScheduleMapper;
import com.akehcloud.iecube.module.lessonschedule.repository.LessonScheduleRepository;
import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import com.akehcloud.iecube.module.resource.service.ResourceService;
import com.akehcloud.iecube.module.studentcourse.dto.HomeworkDTO;
import com.akehcloud.iecube.module.studentcourse.dto.LessonScheduleStatisticsDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.mapper.HomeworkMapper;
import com.akehcloud.iecube.module.studentcourse.qo.HomeworkQO;
import com.akehcloud.iecube.module.studentcourse.service.HomeworkService;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkStatisticsVO;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkVO;
import com.akehcloud.iecube.module.studentcourse.vo.StudentStatisticsVO;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.util.DownloadUtils;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HomeworkServiceImpl implements HomeworkService {

    @Value(value = "${resource-location}")
    private String resourceLocation;

    @Autowired
    private HomeworkMapper homeworkMapper;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private LessonScheduleMapper lessonScheduleMapper;
    @Autowired
    private LessonScheduleRepository lessonScheduleRepository;

    @Override
    public void save(HomeworkDTO dto) {
        AssertUtils.notNull(dto.getLessonScheduleId(), "排课id不能为空");
        AssertUtils.notNull(dto.getKey(), "作业不能为空");
        Boolean exist = homeworkMapper.existByLessonScheduleIdAndStudentId(dto.getLessonScheduleId(), dto.getStudentId());
        if (!exist) {
            throw new UnprocessableException("该课程尚未排课您无需提交作业。");
        }
        Optional<LessonScheduleDO> optional = lessonScheduleRepository.findById(dto.getLessonScheduleId());
        if (optional.isPresent()) {
            LessonScheduleDO lessonScheduleDO = optional.get();
            LocalDate now = LocalDate.now();
            if (now.isBefore(lessonScheduleDO.getStartDate()) && now.isAfter(lessonScheduleDO.getEndDate())) {
                throw new UnprocessableException("不在课程开放时间内，无法提交作业。");
            }
        } else {
            throw new UnprocessableException("无排课信息，无法提交作业。");
        }
        homeworkMapper.update(dto.getKey(), dto.getStudentId(), dto.getLessonScheduleId());
    }

    @Override
    public PageTuple<HomeworkVO> query(HomeworkQO qo) {
        Long count = homeworkMapper.count(qo);
        if (Objects.isNull(count) || count == 0) {
            return PageTuple.empty();
        }
        List<HomeworkVO> list = homeworkMapper.query(qo);
        list.forEach(item -> {
            List<ResourceDTO> attachmentList = resourceService.getAttachmentList(Collections.singletonList(item.getResourceKey()));
            if (CollectionUtils.isNotEmpty(attachmentList)) {
                item.setResource(attachmentList.get(0));
                item.setSubmitHomework(true);
            } else {
                item.setSubmitHomework(false);
            }
        });
        return PageTuple.of(count, list);
    }

    @Override
    public void score(StudentLessonScheduleDTO studentLessonScheduleDTO) {
        homeworkMapper.score(studentLessonScheduleDTO);
    }

    @Override
    public PageTuple<HomeworkStatisticsVO> courseHomeworkStatistics(HomeworkQO qo) {
        Long count = homeworkMapper.homeworkStatisticsCount(qo);
        if (Objects.isNull(count) || count == 0) {
            return PageTuple.empty();
        }
        List<HomeworkStatisticsVO> list = homeworkMapper.homeworkStatisticsList(qo);
        return PageTuple.of(count, list);
    }

    @Override
    public PageTuple<StudentStatisticsVO> homeworkStatistics(HomeworkQO qo) {
        Long count = homeworkMapper.getStudentHomeworkCount(qo);
        if (Objects.isNull(count) || count == 0) {
            return PageTuple.empty();
        }
        List<UserDTO> studentIds = homeworkMapper.getStudentList(qo);
        List<StudentStatisticsVO> list = new ArrayList<>();
        studentIds.forEach(item -> {
            List<LessonScheduleStatisticsDTO> scoreDetails = homeworkMapper.getScoreDetails(qo.getCourseId(), item.getId());
            StudentStatisticsVO vo = ModelUtils.convert(item, StudentStatisticsVO.class);
            vo.setStudentId(item.getId());
            vo.setStudentName(item.getName());
            vo.setLessonHomeworkList(scoreDetails);
            double totalPoints = scoreDetails.stream().mapToDouble(LessonScheduleStatisticsDTO::getScore).sum();
            vo.setTotalPoints(totalPoints);
            double weightTotalPoints = scoreDetails.stream().mapToDouble(value -> value.getScore() * (value.getWeight() / 100)).sum();
            vo.setWeightTotalPoints(weightTotalPoints);
            list.add(vo);
        });
        return PageTuple.of(count, list);
    }

    @Override
    public void zipAndDownload(Long studentId, Long courseId, HttpServletResponse response) {
        // response.setContentType("application/x-msdownload");
        //暂时设定压缩下载后的文件名字为homework.zip
        // response.setHeader("Content-Disposition", "attachment;filename=homework.zip");

        List<LessonScheduleStatisticsDTO> scoreDetails = homeworkMapper.getScoreDetails(courseId, studentId);
        List<String> fileKeys = scoreDetails.stream().map(LessonScheduleStatisticsDTO::getResourceKey).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fileKeys)) {
            return;
        }
        List<ResourceDTO> locations = resourceService.listByKeyIn(fileKeys);

        DownloadUtils.httpDownload("作业.zip", response, out -> {
            StringBuilder listComment = new StringBuilder();
            String rt = System.getProperty("line.separator");
            ZipOutputStream zos;
            try {
                zos = new ZipOutputStream(out);
                for (ResourceDTO resource : locations) {
                    listComment.append(resource).append(rt);
                    File file = new File(this.resourceLocation, resource.getKey());
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(resource.getFilename()));
                    byte[] b = new byte[1024];
                    int n;
                    while ((n = fis.read(b)) != -1) {
                        zos.write(b, 0, n);
                    }
                    zos.flush();
                    fis.close();
                }
                //设置解压文件后的注释内容
                zos.setComment("Homework list:" + rt + listComment);
                zos.flush();
                zos.close();
            } catch (IOException e) {
                throw new SystemException(e);
            }
        });
    }
}
