package com.akehcloud.iecube.module.school.entity;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "school")
public class SchoolDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String homePageBackgroundUrl;
    private String sld;
    private String logoUrl;
    private String adminLogoUrl;
    private Long adminId;
    @Enumerated(EnumType.STRING)
    private EnableStatusEnum status;
    private Long creatorId;
    private Date createTime;
    private Date lastOperateTime;
    private Long lastOperatorId;

}
