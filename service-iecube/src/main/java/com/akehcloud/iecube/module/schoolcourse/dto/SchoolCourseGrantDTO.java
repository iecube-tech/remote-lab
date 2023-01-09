package com.akehcloud.iecube.module.schoolcourse.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SchoolCourseGrantDTO {

    private Long schoolId;
    private Long courseId;
    private Long teacherId;
    private List<Long> teacherIds;
    private Long creatorId;
    private Date grantTime;
}
