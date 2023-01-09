package com.akehcloud.iecube.module.appointment.qo;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class AppointmentQO {
    private Long lessonScheduleId;
    private Long deviceId;
    private LocalDate appointmentDate;
    private Long studentId;
}
