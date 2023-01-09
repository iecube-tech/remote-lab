package com.akehcloud.iecube.module.lesson.mapper;

import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lesson.dto.SimpleLessonDTO;
import com.akehcloud.iecube.module.lesson.entity.LessonDO;
import com.akehcloud.iecube.module.lesson.qo.LessonQO;
import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LessonMapper {

    Long count(LessonQO qo);

    List<LessonDTO> query(LessonQO qo);

    LessonDTO getLessonById(Long id);

    void deleteByCourseId(Long courseId);

    List<LessonDTO> getLessonListByCourseId(Long courseId);

    void modify(LessonDTO dto);

    void delete(Long id);

    void bindLessonAttachmentList(@Param("lessonId") Long lessonId, @Param("attachmentList") List<ResourceDTO> attachmentList);

    void deleteLessonAttachmentList(Long id);

    List<String> getAttachmentKeyList(Long id);

    List<SimpleLessonDTO> getSimpleLessonList(Long courseId);
}
