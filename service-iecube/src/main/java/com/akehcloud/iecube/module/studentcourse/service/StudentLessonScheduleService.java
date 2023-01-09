package com.akehcloud.iecube.module.studentcourse.service;

import com.akehcloud.iecube.enums.lessonschedule.SaveLessonScheduleJoinStudentEnum;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;

import java.util.List;

public interface StudentLessonScheduleService {

    void batchSave(List<StudentLessonScheduleDTO> studentLessonScheduleList, SaveLessonScheduleJoinStudentEnum saveType);

    void deleteByLessonScheduleId(Long lessonScheduleId);

    void deleteByLessonScheduleIdAndStudentIds(Long id, List<Long> studentIds);
}
