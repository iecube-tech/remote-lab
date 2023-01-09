package com.akehcloud.iecube.module.studentcourse.service.impl;

import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.module.appointment.dto.AppointmentDTO;
import com.akehcloud.iecube.module.appointment.service.AppointmentService;
import com.akehcloud.iecube.module.comment.dto.CommentDTO;
import com.akehcloud.iecube.module.comment.qo.CommentQO;
import com.akehcloud.iecube.module.comment.service.CommentService;
import com.akehcloud.iecube.module.course.dto.CourseDTO;
import com.akehcloud.iecube.module.course.service.CourseService;
import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lesson.service.LessonService;
import com.akehcloud.iecube.module.lessonschedule.service.LessonScheduleService;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import com.akehcloud.iecube.module.resource.service.ResourceService;
import com.akehcloud.iecube.module.studentcourse.dto.StudentCourseDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.mapper.StudentCourseMapper;
import com.akehcloud.iecube.module.studentcourse.qo.StudentCourseQO;
import com.akehcloud.iecube.module.studentcourse.service.StudentCourseService;
import com.akehcloud.iecube.module.studentcourse.vo.CourseDetailVO;
import com.akehcloud.iecube.module.studentcourse.vo.SelfCourseDetailVO;
import com.akehcloud.model.PageTuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private StudentCourseMapper studentCourseMapper;
    @Autowired
    private LessonScheduleService lessonScheduleService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ResourceService resourceService;


    @Override
    public PageTuple<StudentCourseDTO> selfCourse(StudentCourseQO qo) {
        Long count = studentCourseMapper.countSelfCourse(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<StudentCourseDTO> list = studentCourseMapper.listSelfCourse(qo);
        return PageTuple.of(count, list);
    }


    @Override
    public void favorite(Long courseId, Long studentId) {
        studentCourseMapper.favorite(courseId, studentId);
    }

    @Override
    public void cancelFavorite(Long courseId, Long studentId) {
        studentCourseMapper.cancelFavorite(courseId, studentId);
    }

    @Override
    public PageTuple<StudentCourseDTO> favoriteList(StudentCourseQO qo) {
        Long favoriteCount = studentCourseMapper.favoriteCount(qo);
        if (Objects.isNull(favoriteCount) || favoriteCount == 0) {
            return PageTuple.empty();
        }
        List<StudentCourseDTO> list = studentCourseMapper.favoriteList(qo);
        return PageTuple.of(favoriteCount, list);
    }

    @Override
    public CourseDetailVO getPublicCourseDetail(StudentCourseQO qo) {
        CourseDetailVO vo = new CourseDetailVO();
        //查课程信息
        CourseDTO courseDTO = courseService.byId(qo.getCourseId());
        if (Objects.isNull(courseDTO.getId())) {
            throw new UnprocessableException("该课程不存在");
        }
        vo.setCourseId(courseDTO.getId());
        vo.setCourseName(courseDTO.getName());
        vo.setSummary(courseDTO.getSummary());
        //是否收藏
        Boolean isFavorite = studentCourseMapper.favoriteExist(qo.getStudentId(), qo.getCourseId());
        vo.setIsFavorite(isFavorite);
        //课节信息
        List<LessonDTO> lessonListByCourseId = lessonService.getLessonListByCourseId(courseDTO.getId());
        vo.setLessonList(lessonListByCourseId);
        //留言信息
        CommentQO commentQO = new CommentQO();
        commentQO.setSchoolId(qo.getSchoolId());
        //用上面课节信息的第一个
        commentQO.setLessonId(lessonListByCourseId.get(0).getId());
        commentQO.withoutPaging();
        PageTuple<CommentDTO> query = commentService.query(commentQO);
        vo.setCommentList(query.getContent());
        return vo;
    }

    @Override
    public CourseDetailVO getCourseDetail(StudentCourseQO qo) {
        CourseDetailVO vo = new CourseDetailVO();
        CourseDTO courseDTO = courseService.byId(qo.getCourseId());
        if (Objects.isNull(courseDTO.getId())) {
            throw new UnprocessableException("该课程不存在");
        }
        vo.setCourseId(courseDTO.getId());
        vo.setCourseName(courseDTO.getName());
        vo.setSummary(courseDTO.getSummary());
        //是否收藏
        Boolean isFavorite = studentCourseMapper.favoriteExist(qo.getStudentId(), qo.getCourseId());
        vo.setIsFavorite(isFavorite);
        return vo;
    }

    @Override
    public List<LessonScheduleVO> listLessonScheduleForStudent(StudentCourseQO qo) {
        return studentCourseMapper.listLessonScheduleForStudent(qo.getStudentId(), qo.getCourseId());
    }

    @Override
    public SelfCourseDetailVO getSelfCourseDetail(StudentCourseQO qo) {
        SelfCourseDetailVO vo = new SelfCourseDetailVO();
        //查课程信息
        CourseDTO courseDTO = courseService.byId(qo.getCourseId());
        if (Objects.isNull(courseDTO.getId())) {
            throw new UnprocessableException("该课程不存在");
        }
        vo.setCourseId(courseDTO.getId());
        vo.setCourseName(courseDTO.getName());
        vo.setSummary(courseDTO.getSummary());
        //是否收藏
        Boolean isFavorite = studentCourseMapper.favoriteExist(qo.getStudentId(), qo.getCourseId());
        vo.setIsFavorite(isFavorite);
        //课节信息
        List<LessonDTO> lessonList = lessonService.getLessonListByCourseId(courseDTO.getId());
        vo.setLessonList(lessonList);
        //当前课节排课信息

        /*for (LessonDTO lessonDTO : lessonList) {
            LessonScheduleVO lessonScheduleVO = this.getLessonScheduleVO(lessonDTO.getId(), qo.getStudentId());
            lessonDTO.setLessonSchedule(lessonScheduleVO);
        }*/

        //留言信息
        CommentQO commentQo = new CommentQO();
        commentQo.setSchoolId(qo.getSchoolId());
        //用上面课节信息的第一个
        commentQo.setLessonId(qo.getLessonId());
        commentQo.withoutPaging();
        PageTuple<CommentDTO> query = commentService.query(commentQo);
        vo.setCommentList(query.getContent());
        return vo;
    }

    @Override
    public LessonScheduleVO getLessonScheduleVO(Long lessonScheduleId, Long currentUserId) {
        LessonScheduleVO lessonScheduleVO = lessonScheduleService.byId(lessonScheduleId);
        //预约和设备信息
        if (CollectionUtils.isNotEmpty(lessonScheduleVO.getDeviceList())) {
            List<AppointmentDTO> appointmentList = appointmentService.getAppointmentByStudentIdAndLessonScheduleId(currentUserId, lessonScheduleId);
            appointmentList.forEach(item -> {
                lessonScheduleVO.getDeviceList().forEach(deviceOperatingDTO -> {
                    if (Objects.equals(item.getDeviceId(), deviceOperatingDTO.getId())) {
                        item.setDeviceOperatingDTO(deviceOperatingDTO);
                    }
                });
            });
            lessonScheduleVO.setAppointmentList(appointmentList);
        }
        //查询学生课节信息
        StudentLessonScheduleDTO scheduleDTO = studentCourseMapper.getStudentCourseDetail(lessonScheduleId, currentUserId);
        lessonScheduleVO.setStudentLessonScheduleDetail(scheduleDTO);
        //作业
        ResourceDTO resourceDTO = resourceService.getByKey(scheduleDTO.getKey());
        lessonScheduleVO.setHomework(resourceDTO);
        return lessonScheduleVO;
    }


    private LessonScheduleVO buildLessonScheduleVO(Long lessonScheduleId, Long studentId) {
        LessonScheduleVO lessonScheduleVO = lessonScheduleService.byId(lessonScheduleId);
        if (Objects.isNull(lessonScheduleVO)) {
            throw new UnprocessableException("排课信息为空");
        }
        //预约和设备信息
        if (CollectionUtils.isNotEmpty(lessonScheduleVO.getDeviceList())) {
            List<AppointmentDTO> appointmentList = appointmentService.getAppointmentByStudentIdAndLessonScheduleId(studentId, lessonScheduleId);
            appointmentList.forEach(item -> {
                lessonScheduleVO.getDeviceList().forEach(deviceOperatingDTO -> {
                    if (Objects.equals(item.getDeviceId(), deviceOperatingDTO.getId())) {
                        item.setDeviceOperatingDTO(deviceOperatingDTO);
                    }
                });
            });
            lessonScheduleVO.setAppointmentList(appointmentList);
        }
        //查询学生课节信息
        StudentLessonScheduleDTO scheduleDTO = studentCourseMapper.getStudentCourseDetail(lessonScheduleId, studentId);
        lessonScheduleVO.setStudentLessonScheduleDetail(scheduleDTO);
        //作业
        ResourceDTO resourceDTO = resourceService.getByKey(scheduleDTO.getKey());
        lessonScheduleVO.setHomework(resourceDTO);
        return lessonScheduleVO;
    }

}
