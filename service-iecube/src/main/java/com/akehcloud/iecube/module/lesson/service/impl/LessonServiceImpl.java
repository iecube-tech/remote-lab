package com.akehcloud.iecube.module.lesson.service.impl;

import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lesson.entity.LessonDO;
import com.akehcloud.iecube.module.lesson.mapper.LessonMapper;
import com.akehcloud.iecube.module.lesson.qo.LessonQO;
import com.akehcloud.iecube.module.lesson.repository.LessonRepository;
import com.akehcloud.iecube.module.lesson.service.LessonService;
import com.akehcloud.iecube.module.resource.service.ResourceService;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ResourceService resourceService;

    @Override
    public List<LessonDTO> getLessonListByCourseId(Long courseId) {
        List<LessonDTO> list = lessonMapper.getLessonListByCourseId(courseId);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public void deleteByCourseId(Long courseId) {
        lessonMapper.deleteByCourseId(courseId);
    }

    @Override
    public void save(LessonDTO dto) {
        AssertUtils.allNotNull("必填参数不能为空", dto.getName(), dto.getSummary(), dto.getCoverUrl(), dto.getContentType());
        LessonDO lessonDO = ModelUtils.convert(dto, LessonDO.class);
        lessonDO.setCreatorId(dto.getCreatorId());
        lessonDO.setCreateTime(new Date());
        lessonDO.setLastOperatorId(dto.getCreatorId());
        lessonDO.setLastOperateTime(new Date());
        lessonRepository.save(lessonDO);
        if (CollectionUtils.isNotEmpty(dto.getAttachmentList())) {
            lessonMapper.bindLessonAttachmentList(lessonDO.getId(), dto.getAttachmentList());
        }
    }

    @Override
    public void modify(LessonDTO dto) {
        AssertUtils.notNull(dto.getId(), "编辑课节id不能为空");
        lessonMapper.modify(dto);
        lessonMapper.deleteLessonAttachmentList(dto.getId());
        if (CollectionUtils.isNotEmpty(dto.getAttachmentList())) {
            lessonMapper.bindLessonAttachmentList(dto.getId(), dto.getAttachmentList());
        }
    }

    @Override
    public LessonDTO byId(Long id) {
        LessonDTO dto = lessonMapper.getLessonById(id);
        List<String> keys = lessonMapper.getAttachmentKeyList(id);
        dto.setAttachmentList(resourceService.listByKeyIn(keys));
        return dto;
    }

    @Override
    public void delete(Long id) {
        List<String> keys = lessonMapper.getAttachmentKeyList(id);
        if (CollectionUtils.isNotEmpty(keys)) {
            resourceService.deleteAttachmentList(keys);
        }
        resourceService.deleteAttachmentList(keys);
        lessonMapper.deleteLessonAttachmentList(id);
        lessonMapper.delete(id);
    }

    @Override
    public PageTuple<LessonDTO> query(LessonQO qo) {
        Long count = lessonMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<LessonDTO> list = lessonMapper.query(qo);
        return PageTuple.of(count, list);
    }

}
