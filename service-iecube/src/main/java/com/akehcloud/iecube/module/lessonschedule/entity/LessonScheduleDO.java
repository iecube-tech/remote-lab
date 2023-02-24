package com.akehcloud.iecube.module.lessonschedule.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity(name = "lesson_schedule")
public class LessonScheduleDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long schoolId;
    private Long lessonId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer dayLimit;
    private Integer appointmentDuration;
    private Integer appointmentCount;
    private String homeworkRequire;
    private Double weight;
    private Long assistantId;
    private Long creatorId;
    private Date creatorTime;
    private Date lastOperateTime;
    private Long lastOperatorId;

}
