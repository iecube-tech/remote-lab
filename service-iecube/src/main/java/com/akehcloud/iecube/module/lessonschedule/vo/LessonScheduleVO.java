package com.akehcloud.iecube.module.lessonschedule.vo;

import com.akehcloud.iecube.module.appointment.dto.AppointmentDTO;
import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class LessonScheduleVO {

    private Long id;
    private Long lessonScheduleId;
    private Long lessonId;
    private String lessonName;
    private Long courseId;
    private String courseName;
    private String experimentOperationPageUrl;
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
    private Long creatorId;
    private String creatorName;
    private Date creatorTime;
    private Date lastOperateTime;
    private Long lastOperatorId;
    private Long assistantId;
    private String assistantName;
    private List<ResourceDTO> homeworkAttachmentList;
    private ResourceDTO homework;
    private List<Long> deviceIds;
    private List<DeviceOperatingDTO> deviceList;
    private List<Long> studentIds;
    private List<UserDTO> studentList;
    private Long studentCount;
    private String teacherName;
    private List<AppointmentDTO> appointmentList;

    private StudentLessonScheduleDTO studentLessonScheduleDetail;


}
