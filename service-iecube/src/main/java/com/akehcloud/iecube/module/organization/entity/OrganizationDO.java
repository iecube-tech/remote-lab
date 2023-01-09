package com.akehcloud.iecube.module.organization.entity;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "organization")
public class OrganizationDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String iconUrl;

    private String coverUrl;

    private Long adminId;

    @Enumerated(value = EnumType.STRING)
    private EnableStatusEnum status;

    private Long creatorId;

    private Date createTime;

    private Date lastOperateTime;

    private Long lastOperatorId;
}
