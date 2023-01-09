package com.akehcloud.iecube.module.resource.repository;

import com.akehcloud.iecube.module.resource.entity.ResourceDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 持久层接口
 *
 * @author wangyaxing
 * @date 2021-05-19
 */
@Repository
public interface ResourceRepository extends JpaRepository<ResourceDO, String> {

    ResourceDO findByKey(String key);

    List<ResourceDO> findByKeyIn(List<String> keyIn);

}
