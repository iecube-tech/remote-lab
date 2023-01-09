package com.akehcloud.iecube.module.course.service;

import com.akehcloud.iecube.module.course.dto.CourseDTO;
import com.akehcloud.iecube.module.course.dto.GrantDTO;
import com.akehcloud.iecube.module.course.qo.CourseQO;
import com.akehcloud.model.PageTuple;

public interface CourseService {

    void save(CourseDTO dto);

    void modify(CourseDTO dto);

    CourseDTO byId(Long id);

    void delete(Long id);

    PageTuple<CourseDTO> query(CourseQO qo);

    void grant(GrantDTO dto);
}
