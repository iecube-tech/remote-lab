package com.akehcloud.iecube.module.comment.service.impl;

import com.akehcloud.iecube.module.comment.repository.ReplyRepository;
import com.akehcloud.iecube.module.comment.dto.ReplyDTO;
import com.akehcloud.iecube.module.comment.entity.ReplyDO;
import com.akehcloud.iecube.module.comment.service.ReplyService;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 回复留言实现
 *
 * @author wangyaxing
 * @date 2021-05-19
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public void save(ReplyDTO replyDTO) {
        ReplyDO replyDO = ModelUtils.convert(replyDTO, ReplyDO.class);
        replyDO.setCreateTime(new Date());
        replyRepository.save(replyDO);
    }
}
