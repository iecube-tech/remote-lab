package com.akehcloud.iecube.module.studentcourse.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StudentCourseDTO {
    private Long courseId;

    private String courseName;

    private Long lessonId;

    private String lessonName;

    private String coverUrl;

    private String summary;

    private Date startDate;

    private Date endDate;

    private String teacherName;

    private Long studentId;
}
