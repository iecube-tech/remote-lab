package com.akehcloud.iecube.enums.user;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum UserTypeEnum {

    USER,
    USER_ORG("COURSE_EDITOR"),
    USER_ORG_ADMIN("ORG_ADMIN"),
    USER_SCHOOL_ADMIN("SCHOOL_ADMIN"),
    TEACHER("TEACHER"),
    STUDENT("STUDENT"),
    ASSISTANT("ASSISTANT");

    @Getter
    private List<String> roleList;

    UserTypeEnum() {

    }

    UserTypeEnum(String role) {
        this.roleList = Arrays.asList(role);
    }

    UserTypeEnum(List<String> roleList) {
        this.roleList = roleList;
    }

}
