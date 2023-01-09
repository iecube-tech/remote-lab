package com.akehcloud.iecube.module.appointment.dto;

import com.akehcloud.iecube.module.device.dto.DeviceDTO;
import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long lessonScheduleId;
    private Long deviceId;
    private LocalDate appointmentDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Boolean status;
    private Long studentId;
    private DeviceOperatingDTO deviceOperatingDTO;
    private DeviceDTO device;
}
