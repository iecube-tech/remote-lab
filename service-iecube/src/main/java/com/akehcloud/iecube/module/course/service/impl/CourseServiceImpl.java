package com.akehcloud.iecube.module.course.service.impl;

import com.akehcloud.iecube.module.course.dto.CourseDTO;
import com.akehcloud.iecube.module.course.dto.GrantDTO;
import com.akehcloud.iecube.module.course.entity.CourseDO;
import com.akehcloud.iecube.module.course.mapper.CourseMapper;
import com.akehcloud.iecube.module.course.qo.CourseQO;
import com.akehcloud.iecube.module.course.service.CourseService;
import com.akehcloud.iecube.module.lesson.service.LessonService;
import com.akehcloud.iecube.module.organization.dto.OrganizationDTO;
import com.akehcloud.iecube.module.organization.repository.OrganizationRepository;
import com.akehcloud.iecube.module.organization.service.OrganizationService;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private OrganizationService organizationService;

    @Override
    public void save(CourseDTO dto) {
        AssertUtils.notNull(dto.getOrgId(),"机构id不能为空");
        AssertUtils.allNotNull("必填参数不能为空", dto.getName(), dto.getCoverUrl(), dto.getSummary());
        CourseDO courseDO = ModelUtils.convert(dto, CourseDO.class);
        courseMapper.save(courseDO);
        courseMapper.saveOrgJoinCourse(dto.getOrgId(),courseDO.getId());
    }

    @Override
    public void modify(CourseDTO dto) {
        AssertUtils.notNull(dto.getId(), "编辑课程信息,课程id不能为空");
        courseMapper.modify(dto);
    }

    @Override
    public CourseDTO byId(Long id) {
        CourseDTO dto = courseMapper.getById(id);
        if (Objects.isNull(dto)) {
            return null;
        }
        if (dto.getOrgId() != null) {
            OrganizationDTO organizationDTO = organizationService.get(dto.getOrgId());
            if (organizationDTO != null) {
                dto.setOrganizationName(organizationDTO.getName());
            }
        }
        return dto;
    }

    @Override
    public void delete(Long id) {
        courseMapper.delete(id);
        lessonService.deleteByCourseId(id);
    }

    @Override
    public PageTuple<CourseDTO> query(CourseQO qo) {
        Long count = courseMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<CourseDTO> list = courseMapper.query(qo);
        return PageTuple.of(count,list);
    }

    @Override
    public void grant(GrantDTO dto) {
        AssertUtils.notNull(dto.getSchoolId(),"学校id不能为空");
        AssertUtils.notEmpty(dto.getCourseIds(), "授权课程不能为空");
        courseMapper.relieveGrant(dto.getSchoolId());
        courseMapper.grant(dto);
    }


}
