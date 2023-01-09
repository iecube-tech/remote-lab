package com.akehcloud.iecube.module.user.controller;

import com.akehcloud.iecube.module.course.dto.SimpleCourseDTO;
import com.akehcloud.iecube.module.lesson.dto.SimpleLessonDTO;
import com.akehcloud.iecube.module.user.service.SelectListService;
import com.akehcloud.iecube.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 各种列表
 *
 * @author wangyaxing
 * @date 2021-05-20
 */
@RequestMapping
@RestController
public class SelectListController {

    @Autowired
    private SelectListService selectListService;


    @GetMapping(value = "/faculty")
    public List<String> facultyList() {
        return selectListService.facultyList(AuthUtils.getCurrentUserSchoolId());
    }
    @GetMapping(value = "/grade")
    public List<String> gradeList() {
        return selectListService.gradeList(AuthUtils.getCurrentUserSchoolId());
    }
    @GetMapping(value = "/grade-class")
    public List<String> gradeClassList() {
        return selectListService.gradeClassList(AuthUtils.getCurrentUserSchoolId());
    }

    @GetMapping(value = "/school/course/simple/list")
    public List<SimpleCourseDTO> getSchoolCourseList(){
        return  selectListService.getSchoolCourseList(AuthUtils.getCurrentUserSchoolId());
    }

    @GetMapping(value = "/teacher/course/simple/list")
    public List<SimpleCourseDTO> getTeacherCourseList(){
        return  selectListService.getTeacherCourseList(AuthUtils.getCurrentUserId());
    }

    @GetMapping(value = "/lesson/simple/list/{courseId}")
    public List<SimpleLessonDTO> getSimpleLessonList(@PathVariable Long courseId){
        return selectListService.getSimpleLessonList(courseId);
    }

    @GetMapping(value = "/lesson-schedule/simple/list/{courseId}")
    public List<SimpleLessonDTO> getSimpleLessonScheduleList(@PathVariable Long courseId){
        return selectListService.getSimpleLessonScheduleList(courseId,AuthUtils.getCurrentUserId());
    }

}
