package com.akehcloud.iecube.module.comment.repository;

import com.akehcloud.iecube.module.comment.entity.CommentDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author wangyaxing
 * @date 2021-05-20
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentDO, Long> {
}
