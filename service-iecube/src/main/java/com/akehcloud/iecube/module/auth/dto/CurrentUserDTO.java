package com.akehcloud.iecube.module.auth.dto;

import com.akehcloud.iecube.enums.user.UserTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class CurrentUserDTO {

    private Long id;
    private String num;
    private String email;
    private String name;
    private String avatarUrl;
    private Long schoolId;
    private String schoolLogoUrl;
    private String schoolAdminLogoUrl;
    private Long organizationId;
    private String faculty;
    private String grade;
    private String gradeClass;
    private UserTypeEnum type;
    private List<String> permissionCodeList;

}
