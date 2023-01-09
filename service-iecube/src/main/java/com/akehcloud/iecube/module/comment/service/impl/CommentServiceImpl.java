package com.akehcloud.iecube.module.comment.service.impl;

import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.iecube.module.comment.dto.CommentDTO;
import com.akehcloud.iecube.module.comment.dto.ReplyDTO;
import com.akehcloud.iecube.module.comment.entity.CommentDO;
import com.akehcloud.iecube.module.comment.entity.ReplyDO;
import com.akehcloud.iecube.module.comment.mapper.CommentMapper;
import com.akehcloud.iecube.module.comment.qo.CommentQO;
import com.akehcloud.iecube.module.comment.repository.CommentRepository;
import com.akehcloud.iecube.module.comment.repository.ReplyRepository;
import com.akehcloud.iecube.module.comment.service.CommentService;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void save(CommentDTO dto) {
        AssertUtils.notNull(dto.getSchoolId(), "学校id不能为空");
        AssertUtils.notNull(dto.getLessonId(), "课节id不能为空");
        AssertUtils.notNull(dto.getContent(), "回复内容不能为空");
        dto.setTop(false);
        CommentDO commentDO = ModelUtils.convert(dto, CommentDO.class);
        commentMapper.save(commentDO);
    }

    @Override
    public PageTuple<CommentDTO> query(CommentQO qo) {
        Long count = commentMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<CommentDTO> list = commentMapper.query(qo);
        return PageTuple.of(count, list);
    }

    @Override
    public CommentDTO detail(Long id) {
        return commentMapper.detailById(id);
    }

    @Override
    public void delete(Long id) {
        commentMapper.deleteById(id);
        replyRepository.deleteByCommentId(id);
    }

    @Override
    public void update(CommentDTO commentDTO) {
        commentMapper.update(commentDTO);
    }

    @Override
    public void top(Long id) {
        Optional<CommentDO> optional = commentRepository.findById(id);
        if (optional.isPresent()) {
            CommentDO commentDO = optional.get();
            commentDO.setTop(!commentDO.getTop());
            commentRepository.save(commentDO);
        } else {
            throw new SystemException("该留言不存在");
        }
    }


}
