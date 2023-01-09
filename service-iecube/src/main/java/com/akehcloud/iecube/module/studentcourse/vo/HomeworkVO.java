package com.akehcloud.iecube.module.studentcourse.vo;

import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class HomeworkVO {
    private Long studentId;
    private String studentName;
    private String num;
    private String faculty;
    private String grade;
    private String gradeClass;
    private Long courseId;
    private String courseName;
    private Long lessonId;
    private String lessonName;
    private String coverUrl;
    private String resourceKey;
    private ResourceDTO resource;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String teacherName;
    private Double score;
    private Boolean submitHomework;
    private Long lessonScheduleId;
}
