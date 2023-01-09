package com.akehcloud.iecube.module.studentcourse.vo;

import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import lombok.Data;

import java.util.List;

@Data
public class LessonDetailVO {

    private List<LessonDTO> lessonList;

    private LessonScheduleVO firstLessonSchedule;
}
