package com.akehcloud.iecube.module.organization.dto;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
public class OrganizationDTO {

    private Long id;

    private String name;

    private String iconUrl;

    private String coverUrl;

    private EnableStatusEnum status;

    private String adminName;

    private String adminEmail;

    private Long creatorId;

    private Date createTime;

    private Date lastOperateTime;

    private Long lastOperatorId;


}
