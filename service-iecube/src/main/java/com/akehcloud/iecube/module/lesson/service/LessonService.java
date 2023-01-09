package com.akehcloud.iecube.module.lesson.service;

import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lesson.qo.LessonQO;
import com.akehcloud.model.PageTuple;

import java.util.List;

public interface LessonService {

    List<LessonDTO> getLessonListByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);

    void save(LessonDTO dto);

    void modify(LessonDTO dto);

    LessonDTO byId(Long id);

    void delete(Long id);

    PageTuple<LessonDTO> query(LessonQO qo);
}
