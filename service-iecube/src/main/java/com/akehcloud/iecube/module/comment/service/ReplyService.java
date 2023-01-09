package com.akehcloud.iecube.module.comment.service;

import com.akehcloud.iecube.module.comment.dto.ReplyDTO;

/**
 * 回复接口
 *
 * @author wangyaxing
 * @date 2021-05-19
 */
public interface ReplyService {
    /**
     * 保存留言回复
     *
     * @param replyDTO 回复dto
     */
    void save(ReplyDTO replyDTO);
}
