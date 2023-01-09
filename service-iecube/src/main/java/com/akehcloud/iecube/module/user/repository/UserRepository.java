package com.akehcloud.iecube.module.user.repository;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import com.akehcloud.iecube.module.user.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepository extends JpaRepository<UserDO, Long> {

    UserDO findByEmail(String email);

    UserDO findByEmailAndStatus(String email, EnableStatusEnum status);

    List<UserDO> findByEmailAndIdIsNot(String email, Long id);

}
