package com.akehcloud.iecube.module.studentcourse.service.impl;

import com.akehcloud.iecube.enums.lessonschedule.SaveLessonScheduleJoinStudentEnum;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.mapper.StudentLessonScheduleMapper;
import com.akehcloud.iecube.module.studentcourse.service.StudentLessonScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class StudentLessonScheduleServiceImpl implements StudentLessonScheduleService {

    @Autowired
    private StudentLessonScheduleMapper studentLessonScheduleMapper;

    @Override
    public void batchSave(List<StudentLessonScheduleDTO> studentLessonScheduleList, SaveLessonScheduleJoinStudentEnum saveType) {
        if (SaveLessonScheduleJoinStudentEnum.SAVE.equals(saveType)){
            studentLessonScheduleMapper.batchSave(studentLessonScheduleList);
        }else {
            Long lessonScheduleId = studentLessonScheduleList.get(0).getLessonScheduleId();
            List<Long> studentIds = studentLessonScheduleMapper.getStudentIdsByLessonScheduleId(lessonScheduleId);
            if (CollectionUtils.isEmpty(studentIds)){
                studentLessonScheduleMapper.batchSave(studentLessonScheduleList);
            }else {
                List<Long> deleteStudentIds = studentIds.stream().filter(item ->
                        studentLessonScheduleList.stream().map(StudentLessonScheduleDTO::getStudentId)
                                .noneMatch(studentId -> Objects.equals(studentId,item))).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(deleteStudentIds)){
                    this.deleteByLessonScheduleIdAndStudentIds(lessonScheduleId,deleteStudentIds);
                }
                List<StudentLessonScheduleDTO> newStudentList = studentLessonScheduleList.stream().filter(item -> !studentIds.contains(item.getStudentId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(newStudentList)){
                    studentLessonScheduleMapper.batchSave(newStudentList);
                }
            }
        }
    }

    @Override
    public void deleteByLessonScheduleId(Long lessonScheduleId) {
        studentLessonScheduleMapper.deleteByLessonScheduleId(lessonScheduleId);
    }

    @Override
    public void deleteByLessonScheduleIdAndStudentIds(Long id, List<Long> studentIds) {
        studentLessonScheduleMapper.deleteByLessonScheduleIdAndStudentIds(id,studentIds);
    }
}
