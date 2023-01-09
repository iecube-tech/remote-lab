package com.akehcloud.iecube.module.resource.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author wangyaxing
 * @date 2021-05-19
 */
@Data
@Entity(name = "resource")
public class ResourceDO {

    @Id
    private String key;
    private String filename;
    private String originFilename;

}
