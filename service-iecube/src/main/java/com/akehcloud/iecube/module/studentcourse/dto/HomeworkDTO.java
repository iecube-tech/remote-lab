package com.akehcloud.iecube.module.studentcourse.dto;

import lombok.Data;

@Data
public class HomeworkDTO {
    private Long lessonScheduleId;
    private Long studentId;
    private String key;
}
