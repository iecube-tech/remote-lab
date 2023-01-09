package com.akehcloud.iecube.module.course.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

import java.util.List;

@Data
public class CourseQO extends AbstractDynamicQO {
    private Long id;
    private Long organizationId;
    private String name;
    private List<Long> ids;
}
