package com.akehcloud.iecube.module.appointment.entity;

import lombok.Data;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
public class AppointmentDO {

    private Long lessonScheduleId;
    private Long deviceId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean status;
    private Long studentId;
}
