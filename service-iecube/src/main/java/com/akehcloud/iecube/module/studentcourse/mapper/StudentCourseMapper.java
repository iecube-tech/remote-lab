package com.akehcloud.iecube.module.studentcourse.mapper;

import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentCourseDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.qo.StudentCourseQO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentCourseMapper {

    Long countSelfCourse(StudentCourseQO qo);

    List<StudentCourseDTO> listSelfCourse(StudentCourseQO qo);


    List<StudentLessonDTO> getLessonByCourseId(StudentCourseQO qo);

    void favorite(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    void cancelFavorite(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    Long favoriteCount(StudentCourseQO qo);

    List<StudentCourseDTO> favoriteList(StudentCourseQO qo);

    Boolean favoriteExist(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    Long getLessonScheduleId(@Param("studentId") Long studentId, @Param("lessonId") Long lessonId);

    StudentLessonScheduleDTO getStudentCourseDetail(@Param("lessonScheduleId") Long lessonScheduleId, @Param("studentId") Long studentId);

    List<LessonScheduleVO> listLessonScheduleForStudent(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
