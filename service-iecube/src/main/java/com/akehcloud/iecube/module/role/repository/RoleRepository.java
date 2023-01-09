package com.akehcloud.iecube.module.role.repository;

import com.akehcloud.iecube.module.role.entity.RoleDO;
import com.akehcloud.iecube.module.role.entity.RoleEmbeddedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleDO, RoleEmbeddedId> {

    List<RoleDO> findByIdSchoolId(Long schoolId);

}
