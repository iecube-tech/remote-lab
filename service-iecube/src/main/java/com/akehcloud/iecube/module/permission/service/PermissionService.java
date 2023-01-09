package com.akehcloud.iecube.module.permission.service;

import com.akehcloud.iecube.module.permission.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {

    List<PermissionDTO> listAll();

    List<String> listCode(String roleCode, Long schoolId);

    List<String> listCodeByUser(Long userId, Long schoolId);

}
