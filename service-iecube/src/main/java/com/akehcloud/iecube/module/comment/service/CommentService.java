package com.akehcloud.iecube.module.comment.service;

import com.akehcloud.iecube.module.comment.dto.CommentDTO;
import com.akehcloud.iecube.module.comment.qo.CommentQO;
import com.akehcloud.model.PageTuple;

public interface CommentService {

    void save(CommentDTO dto);

    PageTuple<CommentDTO> query(CommentQO qo);

    /**
     * 留言详情
     *
     * @param id 留言id
     * @return
     */
    CommentDTO detail(Long id);

    void delete(Long id);

    void update(CommentDTO commentDTO);

    /**
     * 留言置顶
     *
     * @param id
     */
    void top(Long id);

}
