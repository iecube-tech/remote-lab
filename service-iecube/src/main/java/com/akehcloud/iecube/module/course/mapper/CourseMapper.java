package com.akehcloud.iecube.module.course.mapper;

import com.akehcloud.iecube.module.course.dto.CourseDTO;
import com.akehcloud.iecube.module.course.dto.GrantDTO;
import com.akehcloud.iecube.module.course.entity.CourseDO;
import com.akehcloud.iecube.module.course.qo.CourseQO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {

    void save(CourseDO courseDO);

    void modify(CourseDTO dto);

    CourseDTO getById(Long id);

    void delete(Long id);

    void grant(GrantDTO dto);

    Long count(CourseQO qo);

    List<CourseDTO> query(CourseQO qo);

    void saveOrgJoinCourse(@Param("orgId") Long orgId, @Param("courseId") Long courseId);

    void relieveGrant(Long schoolId);
}
