package com.akehcloud.iecube.module.role.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class RoleEmbeddedId implements Serializable {

    private String code;

    private Long schoolId;

}
