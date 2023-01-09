package com.akehcloud.iecube.module.comment.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author akeh
 */
@Data
@Entity
@Table(name = "reply")
public class ReplyDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
