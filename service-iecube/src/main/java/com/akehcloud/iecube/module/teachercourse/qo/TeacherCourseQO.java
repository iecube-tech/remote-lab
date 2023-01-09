package com.akehcloud.iecube.module.teachercourse.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

@Data
public class TeacherCourseQO extends AbstractDynamicQO {

    private String name;
    private Long teacherId;
}
