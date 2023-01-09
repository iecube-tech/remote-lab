package com.akehcloud.iecube.module.teachercourse.service.impl;

import com.akehcloud.iecube.module.teachercourse.dto.TeacherCourseDTO;
import com.akehcloud.iecube.module.teachercourse.mapper.TeacherCourseMapper;
import com.akehcloud.iecube.module.teachercourse.qo.TeacherCourseQO;
import com.akehcloud.iecube.module.teachercourse.service.TeacherCourseService;
import com.akehcloud.model.PageTuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TeacherCourseServiceImpl implements TeacherCourseService {

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Override
    public PageTuple<TeacherCourseDTO> query(TeacherCourseQO qo) {
        Long count = teacherCourseMapper.count(qo);
        if (count == 0){
            return PageTuple.empty();
        }
        List<TeacherCourseDTO> list = teacherCourseMapper.query(qo);
        return PageTuple.of(count,list);
    }
}
