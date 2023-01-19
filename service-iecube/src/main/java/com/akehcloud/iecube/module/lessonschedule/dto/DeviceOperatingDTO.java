package com.akehcloud.iecube.module.lessonschedule.dto;

import lombok.Data;

@Data
public class DeviceOperatingDTO {
    private Long id;
    private String deviceId;
    private String deviceName;
    private String location;
    private Long lessonId;
    private String lessonName;
    private String experimentOperationPageUrl;
    private String signalServerUrl;
    private String signalServerApiKey;
    private String liveUrl;
    private String ysAccessToken;
}
