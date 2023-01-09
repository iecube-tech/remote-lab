package com.akehcloud.iecube.module.schoolcourse.controller;


import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseDTO;
import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseGrantDTO;
import com.akehcloud.iecube.module.schoolcourse.qo.SchoolCourseQO;
import com.akehcloud.iecube.module.schoolcourse.service.SchoolCourseService;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/school-course")
public class SchoolCourseController {

    @Autowired
    private SchoolCourseService schoolCourseService;

    @PostMapping(value = "/query")
    public PageTuple<SchoolCourseDTO> query(@RequestBody SchoolCourseQO qo) {
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        return schoolCourseService.query(qo);
    }

    @PostMapping(value = "/grant")
    public void grant(@RequestBody SchoolCourseGrantDTO schoolCourseGrant) {
        schoolCourseGrant.setCreatorId(AuthUtils.getCurrentUserId());
        schoolCourseGrant.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        schoolCourseService.grant(schoolCourseGrant);
    }

    @PostMapping(value = "/cancel-grant")
    public void cancelGrant(@RequestBody SchoolCourseGrantDTO schoolCourseGrant){
        schoolCourseGrant.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        schoolCourseService.cancelGrant(schoolCourseGrant);
    }

    @GetMapping(value = "/grant-user/{courseId}")
    public List<UserDTO> listGrantUser(@PathVariable Long courseId) {
        return schoolCourseService.listGrantUserByCourseId(AuthUtils.getCurrentUserSchoolId(), courseId);
    }


}
