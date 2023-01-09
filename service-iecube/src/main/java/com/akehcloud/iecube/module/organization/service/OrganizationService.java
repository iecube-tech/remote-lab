package com.akehcloud.iecube.module.organization.service;

import com.akehcloud.iecube.module.organization.dto.OrganizationDTO;
import com.akehcloud.iecube.module.organization.qo.OrganizationQO;
import com.akehcloud.model.PageTuple;

public interface OrganizationService {

    void save(OrganizationDTO dto);

    void modify(OrganizationDTO dto);

    PageTuple<OrganizationDTO> query(OrganizationQO qo);

    OrganizationDTO get(Long id);

}
