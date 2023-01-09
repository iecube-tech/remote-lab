package com.akehcloud.iecube.module.role.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleGrantDTO {

    private Long schoolId;
    private List<RoleDTO> roleList;

}
