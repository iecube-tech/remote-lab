package com.akehcloud.iecube.module.user.dto;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String num;
    private String name;
    private String email;
    private String avatarUrl;
    private String password;
    private EnableStatusEnum status;
    private UserSchoolStatusEnum schoolStatus;
    private UserTypeEnum type;
    private List<String> roleCodeList;
    private Long schoolId;
    private Long organizationId;
    private String faculty;
    private String major;
    private String grade;
    private String gradeClass;
    private Long creatorId;
    private Date createTime;
    private Date lastOperateTime;
    private Long lastOperatorId;
    private String oldPwd;
    private String newPwd;
}
