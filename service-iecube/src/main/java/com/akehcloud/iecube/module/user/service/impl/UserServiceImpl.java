package com.akehcloud.iecube.module.user.service.impl;

import com.akehcloud.exception.runtime.AuthException;
import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.email.EmailSender;
import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import com.akehcloud.iecube.module.role.service.RoleService;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.entity.UserDO;
import com.akehcloud.iecube.module.user.mapper.UserMapper;
import com.akehcloud.iecube.module.user.qo.UserQO;
import com.akehcloud.iecube.module.user.repository.UserRepository;
import com.akehcloud.iecube.module.user.service.UserService;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static final String EMAIL_SUBJECT = "MyElab新用户通知";
    private static final String STUDENT_TEXT = "学生";
    private static final String TEACHER_TEXT = "老师";
    private static final String AT_SCHOOL_TEXT = "在校";
    private static final String LEAVE_SCHOOL_TEXT = "离校";

    @Value("${email.template.user-activate}")
    private Resource userActivateEmail;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDTO get(Long id) {
        AssertUtils.notNull(id, "id不能为空");
        UserDO user = userRepository.getOne(id);
        return ModelUtils.convert(user, UserDTO.class);
    }

    @Override
    public UserDTO save(UserDTO dto) {
        AssertUtils.notNull(dto.getName(), "用户姓名不能为空");
        AssertUtils.notNull(dto.getEmail(), "用户邮箱不能为空");
        AssertUtils.notNull(dto.getType(), "用户类型不能为空");

        UserDO user = userRepository.findByEmail(dto.getEmail());
        AssertUtils.isNull(user, "该邮箱已存在");

        String password = this.generatePassword();
        dto.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));

        dto.setStatus(EnableStatusEnum.ENABLE);
        if (UserTypeEnum.STUDENT.equals(dto.getType()) || UserTypeEnum.TEACHER.equals(dto.getType())) {
            dto.setSchoolStatus(UserSchoolStatusEnum.IN_SCHOOL);
        }
        UserDO userDO = ModelUtils.convert(dto, UserDO.class);
        userDO.setCreateTime(new Date());
        userDO.setLastOperateTime(new Date());
        userDO.setLastOperatorId(dto.getCreatorId());
        userRepository.save(userDO);
        UserTypeEnum type = dto.getType();
        List<String> roleCodeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(type.getRoleList())) {
            roleCodeList.addAll(type.getRoleList());
        }
        if (CollectionUtils.isNotEmpty(dto.getRoleCodeList())) {
            roleCodeList.addAll(dto.getRoleCodeList());
        }
        if (CollectionUtils.isNotEmpty(roleCodeList)) {
            roleService.saveOrUpdate(userDO.getId(), type.getRoleList());
        }
        this.sendEmail(dto, password);
        return ModelUtils.convert(userDO, UserDTO.class);
    }

    private String generatePassword() {
        return ((int) ((Math.random() * 9 + 1) * 100000)) + "";
    }

    public void sendEmail(UserDTO userDTO, String password) {
        String text = this.buildText(userActivateEmail, userDTO.getName(), password);
        emailSender.send(userDTO.getEmail(), EMAIL_SUBJECT, text);
    }

    private String buildText(Resource resource, Object... params) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            FileCopyUtils.copy(resource.getInputStream(), out);
        } catch (IOException e) {
            log.error("IO异常", e);
        }
        String text = out.toString();
        text = MessageFormat.format(text, params);
        return text;
    }

    @Override
    public void modify(UserDTO dto) {
        this.updateValidate(dto);
        Optional<UserDO> byId = userRepository.findById(dto.getId());
        if (byId.isPresent()) {
            UserDO userDO = ModelUtils.convert(dto, UserDO.class);
            userRepository.save(userDO);
        }

    }

    @Override
    public UserDTO getByEmail(String email) {
        AssertUtils.notNull(email, "邮箱不能为空");
        UserDO user = userRepository.findByEmailAndStatus(email, EnableStatusEnum.ENABLE);
        return ModelUtils.convert(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> getUserListByIds(List<Long> studentIds) {
        if (CollectionUtils.isEmpty(studentIds)) {
            return new ArrayList<>();
        }
        List<UserDTO> list = userMapper.getUserListByIds(studentIds);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<UserDTO> list(UserQO qo) {
        return userMapper.list(qo);
    }

    @Override
    public Long count(UserQO qo) {
        return userMapper.count(qo);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        roleService.deleteJoin(id);
    }

    @Override
    public void updateStatus(Long id, EnableStatusEnum status) {
        AssertUtils.notNull(id, "id不能为空");
        AssertUtils.notNull(status, "状态不能为空");
        Optional<UserDO> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDO userDO = user.get();
            userDO.setStatus(status);
            userRepository.save(userDO);
        } else {
            throw new UnprocessableException("用户不存在");
        }
    }

    @Override
    public void updateSchoolStatus(List<Long> idList, UserSchoolStatusEnum schoolStatus) {
        AssertUtils.notEmpty(idList, "id不能为空");
        AssertUtils.notNull(schoolStatus, "状态不能为空");
        List<UserDO> list = userRepository.findAllById(idList);
        if (CollectionUtils.isNotEmpty(list)) {
            for (UserDO userDO : list) {
                userDO.setSchoolStatus(schoolStatus);
            }
            userRepository.saveAll(list);
        }
    }

    @Override
    public void modifyPwd(UserDTO userDTO) {
        Optional<UserDO> optional = userRepository.findById(userDTO.getId());
        if (optional.isPresent()) {
            UserDO userDO = optional.get();
            if (userDO.getPassword().equalsIgnoreCase(
                    DigestUtils.md5DigestAsHex(userDTO.getOldPwd().getBytes(StandardCharsets.UTF_8)))) {
                userDO.setPassword(DigestUtils.md5DigestAsHex(userDTO.getNewPwd().getBytes(StandardCharsets.UTF_8)));
                userRepository.save(userDO);
            } else {
                throw new UnprocessableException("原密码错误");
            }
        } else {
            throw new SystemException("用户不存在");
        }
    }

    @Override
    public void importByExcel(InputStream in, Long currentUserId, Long currentUserSchoolId) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(in);
        } catch (IOException e) {
            log.error("IO异常", e);
            throw new SystemException();
        }
        if (workbook.getNumberOfSheets() < 1) {
            throw new UnprocessableException("至少包含一个Sheet");
        }
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            // 解析row
            for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    if (row.getLastCellNum() < 1) {
                        continue;
                    }
                    UserDTO userDTO = new UserDTO();
                    EnumMap<UserExcelHeaderEnum, String> rowData = this.getRowData(row);
                    userDTO.setNum(rowData.get(UserExcelHeaderEnum.NUM));
                    userDTO.setName(rowData.get(UserExcelHeaderEnum.NAME));
                    userDTO.setEmail(rowData.get(UserExcelHeaderEnum.EMAIL));
                    userDTO.setFaculty(rowData.get(UserExcelHeaderEnum.FACULTY));
                    userDTO.setGrade(rowData.get(UserExcelHeaderEnum.GRADE));
                    userDTO.setGradeClass(rowData.get(UserExcelHeaderEnum.GRADE_CLASS));
                    userDTO.setSchoolId(currentUserSchoolId);
                    userDTO.setStatus(EnableStatusEnum.ENABLE);
                    userDTO.setSchoolStatus(this.parseStatus(rowData.get(UserExcelHeaderEnum.SCHOOL_STATUS)));
                    userDTO.setCreatorId(currentUserId);
                    userDTO.setType(this.parseUserType(rowData.get(UserExcelHeaderEnum.TYPE)));
                    this.save(userDTO);
                }
            }
        }
    }

    @Override
    public void updateNameAndEmail(Long id, String name, String email) {
        List<UserDO> list = userRepository.findByEmailAndIdIsNot(email, id);
        AssertUtils.isEmpty(list, "邮箱已存在");
        Optional<UserDO> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            UserDO userDO = optional.get();
            userDO.setEmail(email);
            userDO.setName(name);
            userRepository.save(userDO);
        }
    }

    @Override
    public void modifyAssistantIdentity(Long id) {
        AssertUtils.notNull(id, "学生id不能为空");
        Optional<UserDO> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            UserDO userDO = optional.get();
            userDO.setType(UserTypeEnum.ASSISTANT);
            userRepository.save(userDO);
        } else {
            throw new UnprocessableException("该用户信息不存在");
        }
        if (CollectionUtils.isNotEmpty(UserTypeEnum.ASSISTANT.getRoleList())) {
            roleService.bindRole(id, UserTypeEnum.ASSISTANT.getRoleList());
        }
    }

    private void updateValidate(UserDTO userDTO) {
        AssertUtils.notNull(userDTO.getId(), "编辑用户信息,id不能为空");
        List<UserDO> list = userRepository.findByEmailAndIdIsNot(userDTO.getEmail(), userDTO.getId());
        AssertUtils.isEmpty(list, "邮箱已存在");
    }

    private EnumMap<UserExcelHeaderEnum, String> getRowData(Row row) {
        EnumMap<UserExcelHeaderEnum, String> map = new EnumMap<>(UserExcelHeaderEnum.class);
        for (UserExcelHeaderEnum col : UserExcelHeaderEnum.values()) {
            Cell cell = row.getCell(col.ordinal());
            String value = this.getStringValue(cell);
            map.put(col, value);
        }
        return map;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return StringUtils.EMPTY;
        }
        return cell.getStringCellValue();
    }

    private UserSchoolStatusEnum parseStatus(String statusText) {
        UserSchoolStatusEnum status;
        switch (statusText) {
            case AT_SCHOOL_TEXT:
                status = UserSchoolStatusEnum.IN_SCHOOL;
                break;
            case LEAVE_SCHOOL_TEXT:
                status = UserSchoolStatusEnum.LEAVE_SCHOOL;
                break;
            default:
                throw new UnprocessableException("不支持的状态");
        }
        return status;
    }

    private UserTypeEnum parseUserType(String roleText) {
        UserTypeEnum type;
        switch (roleText) {
            case STUDENT_TEXT:
                type = UserTypeEnum.STUDENT;
                break;
            case TEACHER_TEXT:
                type = UserTypeEnum.TEACHER;
                break;
            default:
                throw new UnprocessableException("不支持的用户角色");
        }
        return type;
    }

    protected enum UserExcelHeaderEnum {
        /**
         * 编号
         */
        NUM("学号"),
        NAME("名称"),
        EMAIL("邮箱"),
        FACULTY("学院"),
        GRADE("年级"),
        GRADE_CLASS("班级"),
        TYPE("用户类型"),
        SCHOOL_STATUS("状态");
        @Getter
        private final String remark;

        UserExcelHeaderEnum(String remark) {
            this.remark = remark;
        }
    }

}
