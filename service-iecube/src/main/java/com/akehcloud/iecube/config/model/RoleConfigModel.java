package com.akehcloud.iecube.config.model;

import lombok.Data;

import java.util.List;

@Data
public class RoleConfigModel {

    private String code;
    private String name;
    private String appCode;
    private List<String> permissionCodeList;

}
