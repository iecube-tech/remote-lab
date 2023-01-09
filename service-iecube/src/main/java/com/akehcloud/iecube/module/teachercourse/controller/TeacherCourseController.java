package com.akehcloud.iecube.module.teachercourse.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.teachercourse.dto.TeacherCourseDTO;
import com.akehcloud.iecube.module.teachercourse.qo.TeacherCourseQO;
import com.akehcloud.iecube.module.teachercourse.service.TeacherCourseService;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher-course")
public class TeacherCourseController extends BaseController {

    @Autowired
    private TeacherCourseService teacherCourseService;

    @PostMapping("/query")
    public PageTuple<TeacherCourseDTO> query(@RequestBody TeacherCourseQO qo){
        qo.setTeacherId(currentUserId());
        return teacherCourseService.query(qo);
    }

}
