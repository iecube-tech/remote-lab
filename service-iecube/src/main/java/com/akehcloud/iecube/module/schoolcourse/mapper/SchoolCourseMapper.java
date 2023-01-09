package com.akehcloud.iecube.module.schoolcourse.mapper;

import com.akehcloud.iecube.module.course.dto.SimpleCourseDTO;
import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseDTO;
import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseGrantDTO;
import com.akehcloud.iecube.module.schoolcourse.qo.SchoolCourseQO;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SchoolCourseMapper {

    Long count(SchoolCourseQO qo);

    List<SchoolCourseDTO> query(SchoolCourseQO qo);

    void grant(SchoolCourseGrantDTO dto);

    void relieveGrant(@Param("schoolId") Long schoolId, @Param("courseId") Long courseId);

    List<UserDTO> listGrantUser(@Param("schoolId") Long schoolId, @Param("courseId") Long courseId);

    List<SimpleCourseDTO> getSimpleSchoolCourseList(Long schoolId);

    List<SimpleCourseDTO> getSimpleTeacherCourseList(Long teacherId);

    void cancelGrant(SchoolCourseGrantDTO dto);
}
