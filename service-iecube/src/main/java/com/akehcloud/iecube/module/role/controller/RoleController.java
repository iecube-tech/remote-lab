package com.akehcloud.iecube.module.role.controller;

import com.akehcloud.iecube.module.role.dto.RoleDTO;
import com.akehcloud.iecube.module.role.dto.RoleGrantDTO;
import com.akehcloud.iecube.module.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/school-id/{schoolId}")
    public List<RoleDTO> listBySchoolId(@PathVariable Long schoolId) {
        return roleService.listBySchoolId(schoolId);
    }

    @PostMapping(value = "/grant")
    public void grant(@RequestBody RoleGrantDTO roleGant) {
        roleService.grant(roleGant);
    }

}
