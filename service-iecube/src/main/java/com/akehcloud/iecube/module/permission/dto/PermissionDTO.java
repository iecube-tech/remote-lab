package com.akehcloud.iecube.module.permission.dto;

import lombok.Data;

@Data
public class PermissionDTO {

    private String code;
    private String name;
    private String parentCode;
    private String appCode;
    private Long sort;

}
