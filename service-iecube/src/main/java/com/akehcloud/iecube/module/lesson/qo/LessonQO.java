package com.akehcloud.iecube.module.lesson.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

@Data
public class LessonQO extends AbstractDynamicQO {
    private Long courseId;
    private String name;
}
