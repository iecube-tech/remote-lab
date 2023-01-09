package com.akehcloud.iecube.module.user.service.impl;

import com.akehcloud.iecube.module.course.dto.SimpleCourseDTO;
import com.akehcloud.iecube.module.lesson.dto.SimpleLessonDTO;
import com.akehcloud.iecube.module.lesson.mapper.LessonMapper;
import com.akehcloud.iecube.module.lessonschedule.mapper.LessonScheduleMapper;
import com.akehcloud.iecube.module.schoolcourse.mapper.SchoolCourseMapper;
import com.akehcloud.iecube.module.user.dao.UserDAO;
import com.akehcloud.iecube.module.user.service.SelectListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangyaxing
 * @date 2021-05-20
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SelectListServiceImpl implements SelectListService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SchoolCourseMapper schoolCourseMapper;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private LessonScheduleMapper lessonScheduleMapper;

    @Override
    public List<String> facultyList(Long schoolId) {
        return userDAO.findDistinctFaculty(schoolId);
    }

    @Override
    public List<String> gradeList(Long schoolId) {
        return userDAO.findDistinctGrade(schoolId);
    }

    @Override
    public List<String> gradeClassList(Long schoolId) {
        return userDAO.findDistinctGradeClass(schoolId);
    }

    @Override
    public List<SimpleCourseDTO> getSchoolCourseList(Long schoolId) {
        return schoolCourseMapper.getSimpleSchoolCourseList(schoolId);
    }

    @Override
    public List<SimpleCourseDTO> getTeacherCourseList(Long teacherId) {
        return schoolCourseMapper.getSimpleTeacherCourseList(teacherId);
    }

    @Override
    public List<SimpleLessonDTO> getSimpleLessonList(Long courseId) {
        return lessonMapper.getSimpleLessonList(courseId);
    }

    @Override
    public List<SimpleLessonDTO> getSimpleLessonScheduleList(Long courseId, Long teacherId) {
        return lessonScheduleMapper.getSimpleLessonScheduleList(courseId,teacherId);
    }

}
