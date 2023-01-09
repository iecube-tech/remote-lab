package com.akehcloud.iecube.module.schoolcourse.service.impl;

import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseDTO;
import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseGrantDTO;
import com.akehcloud.iecube.module.schoolcourse.mapper.SchoolCourseMapper;
import com.akehcloud.iecube.module.schoolcourse.qo.SchoolCourseQO;
import com.akehcloud.iecube.module.schoolcourse.service.SchoolCourseService;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SchoolCourseServiceImpl implements SchoolCourseService {

    @Autowired
    private SchoolCourseMapper schoolCourseMapper;


    @Override
    public PageTuple<SchoolCourseDTO> query(SchoolCourseQO qo) {
        Long count = schoolCourseMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<SchoolCourseDTO> list = schoolCourseMapper.query(qo);
        return PageTuple.of(count, list);
    }

    @Override
    public void grant(SchoolCourseGrantDTO dto) {
        AssertUtils.notNull(dto.getCourseId(), "课程id不能为空");
        schoolCourseMapper.relieveGrant(dto.getSchoolId(), dto.getCourseId());
        if (CollectionUtils.isNotEmpty(dto.getTeacherIds())) {
            schoolCourseMapper.grant(dto);
        }
    }

    @Override
    public List<UserDTO> listGrantUserByCourseId(Long schoolId, Long courseId) {
        return schoolCourseMapper.listGrantUser(schoolId, courseId);
    }

    @Override
    public void cancelGrant(SchoolCourseGrantDTO dto) {
        AssertUtils.notNull(dto.getCourseId(), "课程id不能为空");
        AssertUtils.notNull(dto.getTeacherId(),"取消授权的老师id不能为空");
        schoolCourseMapper.cancelGrant(dto);
    }

}
