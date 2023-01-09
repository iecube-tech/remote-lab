package com.akehcloud.iecube.module.studentcourse.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

@Data
public class StudentCourseQO extends AbstractDynamicQO {

    private Long courseId;
    private Long schoolId;
    private Long studentId;
    private Long lessonId;
}
