package com.akehcloud.iecube.module.user.service;

import com.akehcloud.iecube.module.course.dto.SimpleCourseDTO;
import com.akehcloud.iecube.module.lesson.dto.SimpleLessonDTO;

import java.util.List;

/**
 * @author wangyaxing
 * @date 2021-05-20
 */
public interface SelectListService {
    List<String> facultyList(Long schoolId);

    List<String> gradeList(Long schoolId);

    List<String> gradeClassList(Long schoolId);

    List<SimpleCourseDTO> getSchoolCourseList(Long schoolId);

    List<SimpleCourseDTO> getTeacherCourseList(Long teacherId);

    List<SimpleLessonDTO> getSimpleLessonList(Long courseId);

    List<SimpleLessonDTO> getSimpleLessonScheduleList(Long courseId, Long teacherId);
}
