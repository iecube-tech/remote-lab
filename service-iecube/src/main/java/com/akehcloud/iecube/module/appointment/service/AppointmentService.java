package com.akehcloud.iecube.module.appointment.service;

import com.akehcloud.iecube.module.appointment.dto.AppointmentDTO;
import com.akehcloud.iecube.module.appointment.qo.AppointmentQO;
import com.akehcloud.iecube.module.appointment.vo.AppointmentVO;
import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.akehcloud.model.PageTuple;

import java.util.List;

public interface AppointmentService {

    void batchSave(List<AppointmentDTO> dtoList);

    void deleteByLessonScheduleId(Long lessonScheduleId);

    void appointment(AppointmentDTO dto);

    List<AppointmentDTO> lessonScheduleAppointmentList(AppointmentQO qo);

    List<AppointmentDTO> getAppointmentByStudentIdAndLessonScheduleId(Long studentId, Long lessonScheduleId);

    PageTuple<AppointmentVO> myAppointment(AppointmentQO qo);

    void cancel(AppointmentDTO dto);

    DeviceOperatingDTO getDeviceOperating(AppointmentDTO dto);

}
