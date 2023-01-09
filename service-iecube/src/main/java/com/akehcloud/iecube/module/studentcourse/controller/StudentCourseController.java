package com.akehcloud.iecube.module.studentcourse.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.course.service.CourseService;
import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lesson.service.LessonService;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentCourseDTO;
import com.akehcloud.iecube.module.studentcourse.qo.StudentCourseQO;
import com.akehcloud.iecube.module.studentcourse.service.StudentCourseService;
import com.akehcloud.iecube.module.studentcourse.vo.CourseDetailVO;
import com.akehcloud.iecube.module.studentcourse.vo.LessonDetailVO;
import com.akehcloud.iecube.module.studentcourse.vo.SelfCourseDetailVO;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/student-course")
public class StudentCourseController extends BaseController {

    @Autowired
    private StudentCourseService studentCourseService;

    /**
     * 学校公开课程的课程详情
     *
     * @param courseId
     * @return
     */
    @GetMapping(value = "/public/course/detail/{courseId}")
    public CourseDetailVO getPublicCourseDetail(@PathVariable Long courseId) {
        StudentCourseQO qo = new StudentCourseQO();
        qo.setStudentId(currentUserId());
        qo.setSchoolId(currentSchoolId());
        qo.setCourseId(courseId);
        qo.withoutPaging();
        return studentCourseService.getPublicCourseDetail(qo);
    }

    @GetMapping("/course/detail/{courseId}")
    public CourseDetailVO getCourseDetail(@PathVariable Long courseId) {
        StudentCourseQO qo = new StudentCourseQO();
        qo.setStudentId(AuthUtils.getCurrentUserId());
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        qo.setCourseId(courseId);
        return studentCourseService.getCourseDetail(qo);
    }

    @GetMapping("/lesson-schedule/course-id/{courseId}")
    public List<LessonScheduleVO> listLessonScheduleForStudent(@PathVariable Long courseId) {
        StudentCourseQO qo = new StudentCourseQO();
        qo.setStudentId(AuthUtils.getCurrentUserId());
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        qo.setCourseId(courseId);
        return studentCourseService.listLessonScheduleForStudent(qo);
    }

    @GetMapping(value = "/lesson-schedule/{lessonScheduleId}")
    public LessonScheduleVO getLessonScheduleVO(@PathVariable Long lessonScheduleId) {
        return studentCourseService.getLessonScheduleVO(lessonScheduleId, AuthUtils.getCurrentUserId());
    }

    /**
     * 我的课程
     *
     * @param qo
     * @return
     */
    @PostMapping(value = "/self")
    public PageTuple<StudentCourseDTO> selfCourse(@RequestBody StudentCourseQO qo) {
        qo.setStudentId(currentUserId());
        qo.setSchoolId(currentSchoolId());
        return studentCourseService.selfCourse(qo);
    }

    @GetMapping(value = "/self/course/detail/{courseId}")
    public SelfCourseDetailVO getSelfCourseDetail(@PathVariable Long courseId) {
        StudentCourseQO qo = new StudentCourseQO();
        qo.setStudentId(currentUserId());
        qo.setSchoolId(currentSchoolId());
        qo.setCourseId(courseId);
        qo.withoutPaging();
        return studentCourseService.getSelfCourseDetail(qo);
    }

    @GetMapping("/favorite/{courseId}")
    public void favorite(@PathVariable Long courseId) {
        studentCourseService.favorite(courseId, currentUserId());
    }

    @GetMapping("/cancel-favorite/{courseId}")
    public void cancelFavorite(@PathVariable Long courseId) {
        studentCourseService.cancelFavorite(courseId, currentUserId());
    }

    @PostMapping("/favorite/query")
    public PageTuple<StudentCourseDTO> favoriteList(@RequestBody StudentCourseQO qo) {
        qo.setStudentId(currentUserId());
        return studentCourseService.favoriteList(qo);
    }


}
