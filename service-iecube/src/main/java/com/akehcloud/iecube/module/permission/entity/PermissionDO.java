package com.akehcloud.iecube.module.permission.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "sys_permission")
public class PermissionDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;
    private String name;
    private String parentCode;
    private String appCode;
    private Long sort;

}
