package com.akehcloud.iecube.module.lessonschedule.service;


import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.akehcloud.iecube.module.lessonschedule.dto.LessonScheduleDTO;
import com.akehcloud.iecube.module.lessonschedule.qo.LessonScheduleQO;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.model.PageTuple;

public interface LessonScheduleService {

    void save(LessonScheduleDTO dto);

    void modify(LessonScheduleDTO dto);

    LessonScheduleVO byId(Long id);

    void delete(Long id);

    PageTuple<LessonScheduleDTO> query(LessonScheduleQO qo);

    DeviceOperatingDTO getDeviceOperating(Long id, Long deviceId);

}
