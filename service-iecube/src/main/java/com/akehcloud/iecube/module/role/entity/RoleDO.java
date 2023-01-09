package com.akehcloud.iecube.module.role.entity;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "sys_role")
public class RoleDO {

    @Id
    @EmbeddedId
    private RoleEmbeddedId id;
    private String name;
    private String appCode;

}
