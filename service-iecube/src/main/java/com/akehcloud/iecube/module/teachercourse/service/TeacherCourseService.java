package com.akehcloud.iecube.module.teachercourse.service;

import com.akehcloud.iecube.module.teachercourse.dto.TeacherCourseDTO;
import com.akehcloud.iecube.module.teachercourse.qo.TeacherCourseQO;
import com.akehcloud.model.PageTuple;

public interface TeacherCourseService {

    PageTuple<TeacherCourseDTO> query(TeacherCourseQO qo);
}
