package com.akehcloud.iecube.module.course.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GrantDTO {

    private Long schoolId;
    
    private List<Long> courseIds;

    private Long creatorId;

    private Date grantTime;

}
