package com.akehcloud.iecube.module.role.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {

    private String code;
    private String name;
    private String appCode;
    private Long schoolId;
    private List<String> permissionCodeList;

}
