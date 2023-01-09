package com.akehcloud.iecube.module.lesson.dto;

import com.akehcloud.iecube.module.lesson.enums.LessonTypeEnum;
import com.akehcloud.iecube.module.lessonschedule.dto.LessonScheduleDTO;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LessonDTO {
    private Long id;
    private Long courseId;
    private String name;
    private String coverUrl;
    private Integer number;
    private String summary;
    private String content;
    private String contentUrl;
    private LessonTypeEnum contentType;
    private String experimentOperationPageUrl;
    private Long creatorId;
    private Date creatorTime;
    private Date lastOperateTime;
    private Long lastOperatorId;

    private String courseName;
    private String creatorName;
    private List<ResourceDTO> attachmentList;
    private LessonScheduleVO lessonSchedule;
    private List<LessonScheduleDTO> lessonScheduleList;

}
