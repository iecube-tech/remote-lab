package com.akehcloud.iecube.module.studentcourse.mapper;

import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentLessonScheduleMapper {

    void batchSave(List<StudentLessonScheduleDTO> studentLessonScheduleList);

    void deleteByLessonScheduleId(Long lessonScheduleId);

    void deleteByLessonScheduleIdAndStudentIds(@Param("lessonScheduleId") Long lessonScheduleId, @Param("studentIds") List<Long> studentIds);

    void addAppointmentCount(@Param("studentId") Long studentId, @Param("lessonScheduleId") Long lessonScheduleId, @Param("count") int count);

    List<Long> getStudentIdsByLessonScheduleId(Long lessonScheduleId);
}
