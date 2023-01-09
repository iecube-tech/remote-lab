package com.akehcloud.iecube.module.device.mapper;

import com.akehcloud.iecube.module.device.dto.DeviceDTO;
import com.akehcloud.iecube.module.device.qo.DeviceQO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {

    Long count(DeviceQO qo);

    List<DeviceDTO> query(DeviceQO qo);

    List<DeviceDTO> getDeviceListByIds(@Param("deviceIds") List<Long> deviceIds);

    void bindCourse(@Param("deviceId") Long deviceId, @Param("courseIdList") List<Long> courseIdList);

    List<Long> listBindCourseId(Long id);

    void deleteBindCourse(Long deviceId);

}
