package com.akehcloud.iecube.module.comment.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author akeh
 */
@Data
public class ReplyDTO {
    private Long id;
    /**
     * 留言id
     */
    private Long commentId;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复时间
     */
    private Date createTime;

    /**
     * 回复人
     */
    private Long creatorId;

    private String creatorName;
}
