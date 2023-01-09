package com.akehcloud.iecube.module.organization.repository;

import com.akehcloud.iecube.module.organization.entity.OrganizationDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

@Mapper
public interface OrganizationRepository extends JpaRepository<OrganizationDO, Long> {
}
