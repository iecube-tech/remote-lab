package com.akehcloud.iecube.module.schoolcourse.service;


import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseDTO;
import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseGrantDTO;
import com.akehcloud.iecube.module.schoolcourse.qo.SchoolCourseQO;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.model.PageTuple;

import java.util.List;

public interface SchoolCourseService {

    PageTuple<SchoolCourseDTO> query(SchoolCourseQO qo);

    void grant(SchoolCourseGrantDTO schoolCourseGrant);

    List<UserDTO> listGrantUserByCourseId(Long schoolId, Long courseId);

    void cancelGrant(SchoolCourseGrantDTO schoolCourseGrant);
}
