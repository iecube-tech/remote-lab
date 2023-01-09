package com.akehcloud.iecube.module.studentcourse.dto;

import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import lombok.Data;

import java.util.Date;

@Data
public class StudentLessonDTO {

    private Long lessonId;

    private String lessonName;

    private String coverUrl;

    private String summary;

    private Date startDate;

    private Date endDate;

    private String teacherName;

    private LessonScheduleVO firstLessonSchedule;
}
