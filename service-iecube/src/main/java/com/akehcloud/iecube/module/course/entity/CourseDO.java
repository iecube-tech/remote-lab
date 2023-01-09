package com.akehcloud.iecube.module.course.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CourseDO {

    private Long id;

    private String name;

    private String coverUrl;

    private String major;

    private String summary;

    private Date createTime;

    private Long creatorId;

    private Date creatorTime;

    private Date lastOperateTime;

    private Long lastOperatorId;

}
