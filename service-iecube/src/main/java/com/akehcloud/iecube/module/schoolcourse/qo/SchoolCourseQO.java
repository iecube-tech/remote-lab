package com.akehcloud.iecube.module.schoolcourse.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

import java.util.List;

@Data
public class SchoolCourseQO extends AbstractDynamicQO {
    private Long id;
    private Long schoolId;
    private String name;
    private List<Long> ids;
}
