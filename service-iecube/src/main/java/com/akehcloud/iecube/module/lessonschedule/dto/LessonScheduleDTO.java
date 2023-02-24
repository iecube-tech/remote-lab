package com.akehcloud.iecube.module.lessonschedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class LessonScheduleDTO {

    private Long id;
    private Long schoolId;
    private Long lessonId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Integer dayLimit;
    private Integer appointmentDuration;
    private Integer appointmentCount;
    private String homeworkRequire;
    private Double weight;
    private Long assistantId;
    private String assistantName;
    private Long creatorId;
    private Date creatorTime;
    private Date lastOperateTime;
    private Long lastOperatorId;
    private List<Long> studentIds;
    private List<String> homeworkAttachmentKeys;
    private List<DeviceOperatingDTO> deviceList;
    private List<Long> deviceIds;
    private Long deviceId;
    private String courseName;
    private String lessonName;
    private Long studentCount;
    private String teacherName;

}
