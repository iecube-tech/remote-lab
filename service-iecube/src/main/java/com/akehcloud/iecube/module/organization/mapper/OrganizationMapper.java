package com.akehcloud.iecube.module.organization.mapper;

import com.akehcloud.iecube.module.organization.dto.OrganizationDTO;
import com.akehcloud.iecube.module.organization.entity.OrganizationDO;
import com.akehcloud.iecube.module.organization.qo.OrganizationQO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrganizationMapper {

    void save(OrganizationDO organizationDO);

    void modify(OrganizationDTO dto);

    void bindOrganizationJoinUser(@Param("userId") Long userId, @Param("orgId") Long orgId);

    Long count(OrganizationQO qo);

    List<OrganizationDTO> query(OrganizationQO qo);

}
