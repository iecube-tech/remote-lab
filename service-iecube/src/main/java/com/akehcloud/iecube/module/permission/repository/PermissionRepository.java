package com.akehcloud.iecube.module.permission.repository;

import com.akehcloud.iecube.module.permission.entity.PermissionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionDO, String> {

}
