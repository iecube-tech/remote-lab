package com.akehcloud.iecube.module.course.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CourseDTO {

    private Long id;

    private Long orgId;

    private String organizationName;

    private String name;

    private String provider;

    private String coverUrl;

    private String major;

    private String summary;

    private Date createTime;

    private Long creatorId;

    private Date lastOperateTime;

    private Long lastOperatorId;

    private String creatorName;

}
