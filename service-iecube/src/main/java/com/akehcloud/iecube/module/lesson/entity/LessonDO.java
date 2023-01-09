package com.akehcloud.iecube.module.lesson.entity;

import com.akehcloud.iecube.module.lesson.enums.LessonTypeEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "lesson")
public class LessonDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private String name;
    private String coverUrl;
    private Integer number;
    private String summary;
    private String content;
    private String contentUrl;
    @Enumerated(value = EnumType.STRING)
    private LessonTypeEnum contentType;
    private String experimentOperationPageUrl;
    private Long creatorId;
    private Date createTime;
    private Date lastOperateTime;
    private Long lastOperatorId;
}
