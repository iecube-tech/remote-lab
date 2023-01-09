package com.akehcloud.iecube.module.comment.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

@Data
public class CommentQO extends AbstractDynamicQO {
    private Long schoolId;
    private String keywords;
    private String num;
    private Long lessonId;
    private Long courseId;
    private String gradeClass;
    private String grade;
    private String faculty;
    private Long teacherId;
}
