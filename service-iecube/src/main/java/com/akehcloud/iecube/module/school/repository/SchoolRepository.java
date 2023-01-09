package com.akehcloud.iecube.module.school.repository;

import com.akehcloud.iecube.module.school.entity.SchoolDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolDO, Long> {
}
