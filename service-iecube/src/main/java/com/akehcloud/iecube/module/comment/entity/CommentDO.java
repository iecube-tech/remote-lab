package com.akehcloud.iecube.module.comment.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name = "comment")
public class CommentDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long lessonId;
    private Long schoolId;
    private String content;
    private Long creatorId;
    private Date createTime;
    private Boolean top;
}
