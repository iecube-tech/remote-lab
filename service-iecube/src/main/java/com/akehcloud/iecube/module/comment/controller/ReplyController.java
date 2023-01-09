package com.akehcloud.iecube.module.comment.controller;

import com.akehcloud.iecube.module.comment.dto.ReplyDTO;
import com.akehcloud.iecube.module.comment.service.ReplyService;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回复有关
 *
 * @author wangyaxing
 * @date 2021-05-19
 */
@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping
    public void save(@RequestBody ReplyDTO replyDTO) {
        AssertUtils.allNotNull("参数错误", replyDTO.getCommentId(), replyDTO.getContent());
        replyDTO.setCreatorId(AuthUtils.getCurrentUserId());
        replyService.save(replyDTO);
    }
}
