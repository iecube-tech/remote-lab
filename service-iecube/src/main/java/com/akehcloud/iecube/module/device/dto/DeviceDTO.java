package com.akehcloud.iecube.module.device.dto;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeviceDTO {

    private Long id;
    private String deviceId;
    private String type;
    private String name;
    private String location;
    private String liveUrl;
    private EnableStatusEnum status;
    private List<Long> courseIdList;
    private Long schoolId;
    private Long creatorId;
    private Date createTime;
    private Date lastOperateTime;
    private Long lastOperatorId;

}
