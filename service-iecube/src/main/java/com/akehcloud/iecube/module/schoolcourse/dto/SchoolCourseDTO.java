package com.akehcloud.iecube.module.schoolcourse.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SchoolCourseDTO {

    private Long id;

    private Long orgId;

    private String provider;

    private Long schoolId;

    private String name;

    private String coverUrl;

    private String major;

    private String summary;

    private Date lastOperateTime;

    private Long lastOperatorId;


}
