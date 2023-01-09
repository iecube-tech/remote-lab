package com.akehcloud.iecube.module.organization.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.organization.dto.OrganizationDTO;
import com.akehcloud.iecube.module.organization.qo.OrganizationQO;
import com.akehcloud.iecube.module.organization.service.OrganizationService;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization")
public class OrganizationController extends BaseController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public void save(@RequestBody OrganizationDTO dto) {
        dto.setCreatorId(currentUserId());
        organizationService.save(dto);
    }

    @PutMapping
    public void modify(@RequestBody OrganizationDTO dto) {
        dto.setCreatorId(currentUserId());
        organizationService.modify(dto);
    }

    @GetMapping(value = "/{id}")
    public OrganizationDTO get(@PathVariable Long id) {
        return organizationService.get(id);
    }

    @PostMapping("/query")
    public PageTuple<OrganizationDTO> query(@RequestBody OrganizationQO qo) {
        return organizationService.query(qo);
    }
}
