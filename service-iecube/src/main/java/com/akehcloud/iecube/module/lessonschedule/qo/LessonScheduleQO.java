package com.akehcloud.iecube.module.lessonschedule.qo;

import com.akehcloud.iecube.enums.lessonschedule.LessonScheduleEnum;
import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

@Data
public class LessonScheduleQO extends AbstractDynamicQO {

    private Long schoolId;
    private Long teacherId;
    private String teacherName;
    private Long courseId;
    private Long lessonId;
    private Long studentId;
    private LessonScheduleEnum timeStatus;
}
