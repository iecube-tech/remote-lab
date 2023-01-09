package com.akehcloud.iecube.module.device.service;

import com.akehcloud.iecube.module.device.dto.DeviceDTO;
import com.akehcloud.iecube.module.device.qo.DeviceQO;
import com.akehcloud.model.PageTuple;

import java.util.List;

public interface DeviceService {

    void save(DeviceDTO dto);

    void modify(DeviceDTO dto);

    PageTuple<DeviceDTO> query(DeviceQO qo);

    List<DeviceDTO> getDeviceListByIds(List<Long> deviceIds);

    DeviceDTO get(Long id);

    void delete(Long id);
}
