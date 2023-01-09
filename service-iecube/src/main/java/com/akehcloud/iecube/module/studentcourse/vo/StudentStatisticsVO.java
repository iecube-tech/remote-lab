package com.akehcloud.iecube.module.studentcourse.vo;

import com.akehcloud.iecube.module.studentcourse.dto.LessonScheduleStatisticsDTO;
import lombok.Data;

import java.util.List;

@Data
public class StudentStatisticsVO {
    private Long studentId;
    private String studentName;
    private String num;
    private String faculty;
    private String grade;
    private String gradeClass;
    private Long courseId;
    private List<LessonScheduleStatisticsDTO> lessonHomeworkList;
    private Double totalPoints;
    private Double weightTotalPoints;
}
