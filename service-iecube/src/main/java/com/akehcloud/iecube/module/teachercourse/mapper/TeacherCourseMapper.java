package com.akehcloud.iecube.module.teachercourse.mapper;

import com.akehcloud.iecube.module.teachercourse.dto.TeacherCourseDTO;
import com.akehcloud.iecube.module.teachercourse.qo.TeacherCourseQO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherCourseMapper {

    Long count(TeacherCourseQO qo);

    List<TeacherCourseDTO> query(TeacherCourseQO qo);
}
