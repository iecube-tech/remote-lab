package com.akehcloud.iecube.module.device.repository;

import com.akehcloud.iecube.module.device.entity.DeviceDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wangyaxing
 * @date 2021-05-20
 */
@Repository
public interface DeviceRepository extends JpaRepository<DeviceDO, Long> {
}
