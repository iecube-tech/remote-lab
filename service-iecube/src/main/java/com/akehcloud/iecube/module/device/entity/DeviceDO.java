package com.akehcloud.iecube.module.device.entity;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "device")
public class DeviceDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviceId;
    private String type;
    private String name;
    private String location;
    private String liveUrl;
    @Enumerated(EnumType.STRING)
    private EnableStatusEnum status;
    private Long schoolId;
    private Long creatorId;
    private Date createTime;
    private Date lastOperateTime;
    private Long lastOperatorId;

}
