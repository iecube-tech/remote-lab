package com.akehcloud.iecube.module.auth.service.impl;

import com.akehcloud.exception.runtime.AuthException;
import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.config.InitDataProperties;
import com.akehcloud.iecube.config.model.RoleConfigModel;
import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import com.akehcloud.iecube.module.auth.dto.CurrentUserDTO;
import com.akehcloud.iecube.module.auth.dto.LoginDTO;
import com.akehcloud.iecube.module.auth.dto.LoginResultDTO;
import com.akehcloud.iecube.module.auth.service.AuthService;
import com.akehcloud.iecube.module.organization.dto.OrganizationDTO;
import com.akehcloud.iecube.module.organization.service.OrganizationService;
import com.akehcloud.iecube.module.permission.service.PermissionService;
import com.akehcloud.iecube.module.school.dto.SchoolDTO;
import com.akehcloud.iecube.module.school.service.SchoolService;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.service.UserService;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private InitDataProperties initDataProperties;

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private OrganizationService orgService;

    @Override
    public LoginResultDTO login(LoginDTO loginDTO) {
        AssertUtils.hasText(loginDTO.getEmail(), "邮箱不能为空");
        AssertUtils.hasText(loginDTO.getPassword(), "密码不能为空");
        UserDTO user = userService.getByEmail(loginDTO.getEmail());
        AssertUtils.notNull(user, "用户不存在或未启用");
        String inMd5 = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        if (Objects.equals(inMd5, user.getPassword())) {
            // 登录信息正确，校验相关状态
            CurrentUserDTO currentUser = ModelUtils.convert(user, CurrentUserDTO.class);
            this.checkRelatedStatus(user, currentUser);
            List<String> allPermissions = new ArrayList<>();
            List<String> permissionCodeList = permissionService.listCodeByUser(user.getId(), user.getSchoolId());
            if (CollectionUtils.isNotEmpty(permissionCodeList)) {
                allPermissions.addAll(permissionCodeList);
            }
            if (UserTypeEnum.USER_ORG.equals(user.getType()) || UserTypeEnum.USER_ORG_ADMIN.equals(user.getType())) {
                List<RoleConfigModel> organizationRoles = initDataProperties.getOrganizationRoles();
                if (CollectionUtils.isNotEmpty(organizationRoles)) {
                    List<List<String>> list = organizationRoles
                            .stream()
                            .filter(o -> user.getType().getRoleList().contains(o.getCode()))
                            .map(RoleConfigModel::getPermissionCodeList)
                            .collect(Collectors.toList());
                    for (List<String> codeList : list) {
                        allPermissions.addAll(codeList);
                    }
                }
            }
            currentUser.setPermissionCodeList(allPermissions);
            return LoginResultDTO.result(
                    AuthUtils.createToken(currentUser.getId()),
                    currentUser
            );
        }
        throw new AuthException("用户名或密码错误", 10003);
    }

    /**
     * 校验学校、用户、机构状态
     *
     * @param user
     */
    private void checkRelatedStatus(UserDTO user, CurrentUserDTO currentUser) {
        if (Objects.nonNull(user.getSchoolStatus())) {
            // 学校用户，校验学校状态及用户状态
            SchoolDTO schoolDTO = schoolService.get(user.getSchoolId());
            AssertUtils.notNull(schoolDTO, "用户所属学校信息错误");
            if (EnableStatusEnum.DISABLE.equals(schoolDTO.getStatus())) {
                throw new UnprocessableException("用户所属学校未启用");
            }
            if (UserSchoolStatusEnum.LEAVE_SCHOOL.equals(user.getSchoolStatus())) {
                throw new UnprocessableException("用户已离校");
            }
            currentUser.setSchoolLogoUrl(schoolDTO.getLogoUrl());
            currentUser.setSchoolAdminLogoUrl(schoolDTO.getAdminLogoUrl());
        } else {
            // 机构用户，校验机构状态
            if (user.getOrganizationId() != 0) {
                OrganizationDTO organizationDTO = orgService.get(user.getOrganizationId());
                AssertUtils.notNull(organizationDTO, "用户所属机构信息错误");
                if (EnableStatusEnum.DISABLE.equals(organizationDTO.getStatus())) {
                    throw new UnprocessableException("用户所属机构未启用");
                }
            }
        }
    }

}
