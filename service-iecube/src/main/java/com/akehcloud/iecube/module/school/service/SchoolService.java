package com.akehcloud.iecube.module.school.service;

import com.akehcloud.iecube.module.course.dto.SimpleCourseDTO;
import com.akehcloud.iecube.module.school.dto.SchoolDTO;
import com.akehcloud.iecube.module.school.qo.SchoolQO;

import java.util.List;

public interface SchoolService {

    void save(SchoolDTO dto);

    void modify(SchoolDTO dto);

    List<SchoolDTO> list(SchoolQO qo);

    Long count(SchoolQO qo);

    SchoolDTO get(Long id);

    List<SimpleCourseDTO> listGrantCourse(Long schoolId);

}
