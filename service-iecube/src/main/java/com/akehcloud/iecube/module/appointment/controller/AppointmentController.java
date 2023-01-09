package com.akehcloud.iecube.module.appointment.controller;

import com.akehcloud.iecube.module.appointment.dto.AppointmentDTO;
import com.akehcloud.iecube.module.appointment.qo.AppointmentQO;
import com.akehcloud.iecube.module.appointment.service.AppointmentService;
import com.akehcloud.iecube.module.appointment.vo.AppointmentVO;
import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public void appointment(@RequestBody AppointmentDTO dto) {
        dto.setStudentId(AuthUtils.getCurrentUserId());
        appointmentService.appointment(dto);
    }

    @PostMapping(value = "/cancel")
    public void cancel(@RequestBody AppointmentDTO dto) {
        dto.setStudentId(AuthUtils.getCurrentUserId());
        appointmentService.cancel(dto);
    }

    @PostMapping(value = "/device/operating")
    public DeviceOperatingDTO getDeviceOperating(@RequestBody AppointmentDTO dto) {
        return appointmentService.getDeviceOperating(dto);
    }

    @PostMapping(value = "/query/lesson-schedule")
    public List<AppointmentDTO> lessonScheduleAppointmentList(@RequestBody AppointmentQO qo) {
        return appointmentService.lessonScheduleAppointmentList(qo);
    }

    @PostMapping(value = "/my-appointment")
    public PageTuple<AppointmentVO> myAppointment(@RequestBody AppointmentQO qo) {
        qo.setStudentId(AuthUtils.getCurrentUserId());
        return appointmentService.myAppointment(qo);
    }


}
