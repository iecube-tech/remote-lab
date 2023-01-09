package com.akehcloud.iecube.module.appointment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
public class AppointmentVO {
    private Long lessonId;
    private String lessonName;
    private String lessonCoverUrl;
    private Long lessonScheduleId;
    private Long  appointmentId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Long deviceId;
    private String deviceName;
}
