package com.akehcloud.iecube.module.comment.repository;

import com.akehcloud.iecube.module.comment.entity.ReplyDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 持久层接口
 *
 * @author wangyaxing
 * @date 2021-05-19
 */
@Repository
public interface ReplyRepository extends JpaRepository<ReplyDO, Long> {

    void deleteByCommentId(Long commentId);

    ReplyDO findByCommentId(Long commentId);

}
