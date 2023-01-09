package com.akehcloud.iecube.module.studentcourse.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

import java.util.List;

@Data
public class HomeworkQO extends AbstractDynamicQO {

    private Long schoolId;
    private Long studentId;
    private String studentName;
    private String num;
    private String keywords;
    private String faculty;
    private String grade;
    private String gradeClass;
    private Long courseId;
    private String courseName;
    private Long lessonId;
    private String lessonName;
    private Long teacherId;
    private String teacherName;
    private List<Long> courseIds;
}
