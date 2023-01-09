package com.akehcloud.iecube.module.user.entity;


import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "user_info")
public class UserDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String num;
    private String name;
    private String email;
    private String avatarUrl;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserTypeEnum type;
    @Enumerated(EnumType.STRING)
    private EnableStatusEnum status;
    @Enumerated(EnumType.STRING)
    private UserSchoolStatusEnum schoolStatus;
    private Long schoolId;
    private Long organizationId;
    private String faculty;
    private String major;
    /**
     * 年级
     */
    private String grade;
    /**
     * 班级
     */
    private String gradeClass;
    private Long creatorId;
    @CreatedDate
    private Date createTime;
    private Long lastOperatorId;
    @LastModifiedDate
    private Date lastOperateTime;

}
