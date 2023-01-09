package com.akehcloud.iecube.module.studentcourse.controller;

import com.akehcloud.iecube.module.studentcourse.dto.HomeworkDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.qo.HomeworkQO;
import com.akehcloud.iecube.module.studentcourse.service.HomeworkService;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkStatisticsVO;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkVO;
import com.akehcloud.iecube.module.studentcourse.vo.StudentStatisticsVO;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    @PostMapping
    public void save(@RequestBody HomeworkDTO dto) {
        dto.setStudentId(AuthUtils.getCurrentUserId());
        homeworkService.save(dto);
    }

    @PostMapping(value = "/query")
    public PageTuple<HomeworkVO> query(@RequestBody HomeworkQO qo) {
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        qo.setTeacherId(AuthUtils.getCurrentUserId());
        return homeworkService.query(qo);
    }

    @PostMapping(value = "/self")
    public PageTuple<HomeworkVO> self(@RequestBody HomeworkQO qo) {
        qo.setStudentId(AuthUtils.getCurrentUserId());
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        qo.withoutPaging();
        return homeworkService.query(qo);
    }

    @PostMapping(value = "/course/statistics")
    public PageTuple<HomeworkStatisticsVO> courseHomeworkStatistics(@RequestBody HomeworkQO qo) {
        qo.setTeacherId(AuthUtils.getCurrentUserId());
        return homeworkService.courseHomeworkStatistics(qo);
    }

    @PostMapping(value = "/statistics")
    public PageTuple<StudentStatisticsVO> homeworkStatistics(@RequestBody HomeworkQO qo) {
        qo.setTeacherId(AuthUtils.getCurrentUserId());
        return homeworkService.homeworkStatistics(qo);
    }

    @PutMapping(value = "/score")
    public void score(@RequestBody StudentLessonScheduleDTO studentLessonScheduleDTO) {
        AssertUtils.allNotNull("参数错误", studentLessonScheduleDTO.getStudentId(),
                studentLessonScheduleDTO.getLessonScheduleId());
        homeworkService.score(studentLessonScheduleDTO);
    }

    @GetMapping(value = "/packaging/{studentId}/{courseId}")
    public void zipAndDownload(@PathVariable Long studentId, @PathVariable Long courseId, HttpServletResponse response) {
        homeworkService.zipAndDownload(studentId, courseId, response);
    }
}
