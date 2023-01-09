package com.akehcloud.iecube.module.studentcourse.service;

import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentCourseDTO;
import com.akehcloud.iecube.module.studentcourse.qo.StudentCourseQO;
import com.akehcloud.iecube.module.studentcourse.vo.CourseDetailVO;
import com.akehcloud.iecube.module.studentcourse.vo.LessonDetailVO;
import com.akehcloud.iecube.module.studentcourse.vo.SelfCourseDetailVO;
import com.akehcloud.model.PageTuple;

import java.util.List;

public interface StudentCourseService {

    PageTuple<StudentCourseDTO> selfCourse(StudentCourseQO qo);

    void favorite(Long courseId, Long studentId);

    void cancelFavorite(Long courseId, Long studentId);

    PageTuple<StudentCourseDTO> favoriteList(StudentCourseQO qo);

    CourseDetailVO getPublicCourseDetail(StudentCourseQO qo);

    SelfCourseDetailVO getSelfCourseDetail(StudentCourseQO qo);

    LessonScheduleVO getLessonScheduleVO(Long lessonScheduleId, Long currentUserId);

    CourseDetailVO getCourseDetail(StudentCourseQO qo);

    List<LessonScheduleVO> listLessonScheduleForStudent(StudentCourseQO qo);
}
