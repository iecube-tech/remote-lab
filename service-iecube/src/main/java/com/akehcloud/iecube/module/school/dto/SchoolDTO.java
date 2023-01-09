package com.akehcloud.iecube.module.school.dto;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
public class SchoolDTO {

    private Long id;
    private String name;
    private String homePageBackgroundUrl;
    private String sld;
    private String logoUrl;
    private String adminLogoUrl;
    private EnableStatusEnum status;
    private Long adminId;
    private String adminName;
    private String adminEmail;
    private Long creatorId;
    private Date createTime;
    private Date lastOperateTime;
    private Long lastOperatorId;

}
