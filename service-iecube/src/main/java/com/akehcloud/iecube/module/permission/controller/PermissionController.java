package com.akehcloud.iecube.module.permission.controller;

import com.akehcloud.iecube.module.permission.dto.PermissionDTO;
import com.akehcloud.iecube.module.permission.service.PermissionService;
import com.akehcloud.iecube.module.role.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/all")
    public List<PermissionDTO> listAll() {
        return permissionService.listAll();
    }

}
