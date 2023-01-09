package com.akehcloud.iecube.module.device.service.impl;

import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.module.device.dto.DeviceDTO;
import com.akehcloud.iecube.module.device.entity.DeviceDO;
import com.akehcloud.iecube.module.device.mapper.DeviceMapper;
import com.akehcloud.iecube.module.device.qo.DeviceQO;
import com.akehcloud.iecube.module.device.repository.DeviceRepository;
import com.akehcloud.iecube.module.device.service.DeviceService;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void save(DeviceDTO dto) {
        AssertUtils.notNull(dto.getName(), "设备名称不能为空");
        AssertUtils.notNull(dto.getLocation(), "设备位置不能为空");
        AssertUtils.notNull(dto.getLiveUrl(), "直播地址不能为空");
        DeviceDO deviceDO = ModelUtils.convert(dto, DeviceDO.class);
        deviceDO.setCreateTime(new Date());
        deviceDO.setLastOperateTime(new Date());
        deviceRepository.save(deviceDO);
        if (CollectionUtils.isNotEmpty(dto.getCourseIdList())) {
            deviceMapper.bindCourse(deviceDO.getId(), dto.getCourseIdList());
        }
    }

    @Override
    public void modify(DeviceDTO dto) {
        AssertUtils.notNull(dto.getId(), "设备id不能为空");
        Optional<DeviceDO> optional = deviceRepository.findById(dto.getId());
        if (optional.isPresent()) {
            DeviceDO deviceDO = optional.get();
            deviceDO.setDeviceId(dto.getDeviceId());
            deviceDO.setName(dto.getName());
            deviceDO.setType(dto.getType());
            deviceDO.setLocation(dto.getLocation());
            deviceDO.setLiveUrl(dto.getLiveUrl());
            deviceDO.setStatus(dto.getStatus());
            deviceDO.setLastOperatorId(dto.getLastOperatorId());
            deviceDO.setLastOperateTime(new Date());
            deviceMapper.deleteBindCourse(deviceDO.getId());
            if (CollectionUtils.isNotEmpty(dto.getCourseIdList())) {
                deviceMapper.bindCourse(deviceDO.getId(), dto.getCourseIdList());
            }
            deviceRepository.save(deviceDO);
        } else {
            throw new UnprocessableException("该设备不存在");
        }
    }

    @Override
    public PageTuple<DeviceDTO> query(DeviceQO qo) {
        Long count = deviceMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<DeviceDTO> list = deviceMapper.query(qo);
        return PageTuple.of(count, list);
    }

    @Override
    public List<DeviceDTO> getDeviceListByIds(List<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return new ArrayList<>();
        }
        List<DeviceDTO> dtoList = deviceMapper.getDeviceListByIds(deviceIds);
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        return dtoList;
    }

    @Override
    public DeviceDTO get(Long id) {
        Optional<DeviceDO> optional = deviceRepository.findById(id);
        DeviceDTO deviceDTO = optional.map(deviceDO -> ModelUtils.convert(deviceDO, DeviceDTO.class)).orElse(null);
        if (deviceDTO == null) {
            return null;
        }
        List<Long> courseIdList = deviceMapper.listBindCourseId(deviceDTO.getId());
        deviceDTO.setCourseIdList(courseIdList);
        return deviceDTO;
    }

    @Override
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }

}
