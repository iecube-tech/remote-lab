package com.akehcloud.iecube.module.comment.mapper;

import com.akehcloud.iecube.module.comment.dto.CommentDTO;
import com.akehcloud.iecube.module.comment.entity.CommentDO;
import com.akehcloud.iecube.module.comment.qo.CommentQO;
import com.akehcloud.iecube.module.lessonschedule.dto.LessonScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    void save(CommentDO commentDO);

    Long count(CommentQO qo);

    List<CommentDTO> query(CommentQO qo);

    CommentDTO detailById(Long id);

    void deleteById(Long id);

    void update(CommentDTO commentDTO);

    LessonScheduleDTO LessonScheduleDetail(CommentQO qo);
}
