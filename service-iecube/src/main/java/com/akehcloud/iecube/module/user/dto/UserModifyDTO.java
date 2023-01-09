package com.akehcloud.iecube.module.user.dto;

import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class UserModifyDTO {

    private List<Long> idList;
    private UserSchoolStatusEnum schoolStatus;

}
