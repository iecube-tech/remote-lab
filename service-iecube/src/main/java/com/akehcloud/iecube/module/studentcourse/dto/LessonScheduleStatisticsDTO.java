package com.akehcloud.iecube.module.studentcourse.dto;

import lombok.Data;

@Data
public class LessonScheduleStatisticsDTO {
    private Long lessonId;
    private String lessonName;
    private Long lessonScheduleId;
    private Double score;
    private Double weight;
    private String resourceKey;
}
