package com.akehcloud.iecube.module.device.qo;

import com.akehcloud.model.AbstractDynamicQO;
import lombok.Data;

@Data
public class DeviceQO extends AbstractDynamicQO {

    private Long schoolId;
    private Long creatorId;
    private Long courseId;
    private String name;
    private String location;

}
