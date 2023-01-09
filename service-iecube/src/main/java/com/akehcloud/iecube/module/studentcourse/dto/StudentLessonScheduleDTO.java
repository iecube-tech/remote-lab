package com.akehcloud.iecube.module.studentcourse.dto;

import lombok.Data;

@Data
public class StudentLessonScheduleDTO {

    private Long id;
    private Long studentId;
    private Long lessonScheduleId;
    private Double score;
    private Integer appointmentCount;
    private String key;

}
