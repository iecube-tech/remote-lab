package com.akehcloud.iecube.module.school.service.impl;

import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.config.InitDataProperties;
import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import com.akehcloud.iecube.module.course.dto.SimpleCourseDTO;
import com.akehcloud.iecube.module.role.dto.RoleDTO;
import com.akehcloud.iecube.module.role.service.RoleService;
import com.akehcloud.iecube.module.school.dto.SchoolDTO;
import com.akehcloud.iecube.module.school.entity.SchoolDO;
import com.akehcloud.iecube.module.school.mapper.SchoolMapper;
import com.akehcloud.iecube.module.school.qo.SchoolQO;
import com.akehcloud.iecube.module.school.repository.SchoolRepository;
import com.akehcloud.iecube.module.school.service.SchoolService;
import com.akehcloud.iecube.module.schoolcourse.dto.SchoolCourseDTO;
import com.akehcloud.iecube.module.schoolcourse.qo.SchoolCourseQO;
import com.akehcloud.iecube.module.schoolcourse.service.SchoolCourseService;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.service.UserService;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private InitDataProperties initDataProperties;

    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SchoolCourseService schoolCourseService;


    @Override
    public void save(SchoolDTO dto) {
        AssertUtils.notNull(dto.getName(), "学校名称不能为空");
        AssertUtils.notNull(dto.getAdminEmail(), "管理员邮箱不能为空");
        AssertUtils.notNull(dto.getAdminName(), "管理员姓名不能为空");
        SchoolDO schoolDO = ModelUtils.convert(dto, SchoolDO.class);
        schoolDO.setCreatorId(dto.getCreatorId());
        schoolDO.setCreateTime(new Date());
        schoolDO.setLastOperatorId(dto.getCreatorId());
        schoolDO.setLastOperateTime(new Date());
        SchoolDO school = schoolRepository.save(schoolDO);

        UserDTO byEmail = userService.getByEmail(dto.getAdminEmail());
        if (Objects.nonNull(byEmail) && UserTypeEnum.USER_SCHOOL_ADMIN.equals(byEmail.getType())) {
            throw new UnprocessableException("该管理员已绑定其他学校");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setCreatorId(dto.getCreatorId());
        userDTO.setNum("0");
        userDTO.setName(dto.getAdminName());
        userDTO.setEmail(dto.getAdminEmail());
        userDTO.setType(UserTypeEnum.USER_SCHOOL_ADMIN);
        userDTO.setSchoolStatus(UserSchoolStatusEnum.IN_SCHOOL);
        userDTO.setSchoolId(school.getId());
        UserDTO user = userService.save(userDTO);
        school.setAdminId(user.getId());
        schoolRepository.save(school);
        roleService.batchSave(ModelUtils.converts(initDataProperties.getSchoolRoles(), RoleDTO.class), school.getId());
    }

    @Override
    public void modify(SchoolDTO dto) {
        AssertUtils.notNull(dto.getId(), "学校id信息不能为空");
        schoolMapper.modify(dto);
        Optional<SchoolDO> optional = schoolRepository.findById(dto.getId());
        if (optional.isPresent()) {
            SchoolDO schoolDO = optional.get();
            Long adminId = schoolDO.getAdminId();
            userService.updateNameAndEmail(adminId, dto.getAdminName(), dto.getAdminEmail());
        }
    }

    @Override
    public List<SchoolDTO> list(SchoolQO qo) {
        return schoolMapper.list(qo);
    }

    @Override
    public Long count(SchoolQO qo) {
        return schoolMapper.count(qo);
    }

    @Override
    public SchoolDTO get(Long id) {
        AssertUtils.notNull(id, "id不能为空");
        SchoolDO school = schoolRepository.getOne(id);
        SchoolDTO schoolDTO = ModelUtils.convert(school, SchoolDTO.class);
        if (schoolDTO != null && schoolDTO.getAdminId() != null) {
            UserDTO userDTO = userService.get(schoolDTO.getAdminId());
            if (userDTO != null) {
                schoolDTO.setAdminName(userDTO.getName());
                schoolDTO.setAdminEmail(userDTO.getEmail());
            }
        }
        return schoolDTO;
    }

    @Override
    public List<SimpleCourseDTO> listGrantCourse(Long schoolId) {
        SchoolCourseQO qo = new SchoolCourseQO();
        qo.setSchoolId(schoolId);
        qo.withoutPaging();
        PageTuple<SchoolCourseDTO> pageTuple = schoolCourseService.query(qo);
        return ModelUtils.converts(pageTuple.getContent(), SimpleCourseDTO.class);
    }

}
