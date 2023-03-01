package com.akehcloud.iecube.module.user.qo;

import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

import java.util.List;

@Data
public class UserQO extends AbstractDynamicQO {

    private List<Long> idIn;
    private long id;
    private String keywords;
    private String num;
    private String faculty;
    private String grade;
    private String gradeClass;
    private Long schoolId;
    private Long organizationId;
    private UserTypeEnum type;
    private List<UserTypeEnum> typeList;
    private UserSchoolStatusEnum schoolStatus;

}
