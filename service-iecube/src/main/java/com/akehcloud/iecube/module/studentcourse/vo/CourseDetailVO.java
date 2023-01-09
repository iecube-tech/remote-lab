package com.akehcloud.iecube.module.studentcourse.vo;

import com.akehcloud.iecube.module.comment.dto.CommentDTO;
import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import lombok.Data;

import java.util.List;

@Data
public class CourseDetailVO {
    private Long courseId;
    private String courseName;
    private String summary;
    private Boolean isFavorite;
    private List<LessonDTO> lessonList;
    private List<CommentDTO> commentList;
}
