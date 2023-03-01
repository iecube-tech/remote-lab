package com.akehcloud.iecube.module.comment.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    private Long id;
    private Long lessonId;
    private Long schoolId;
    private String content;
    private Long creatorId;
    private Date createTime;
    private Boolean top;
    private long lessonScheduleID;
    private String lessonName;
    private String creatorName;
    private String email;
    private String courseName;
    /**
     * 留言
     */
    private ReplyDTO reply;
}
