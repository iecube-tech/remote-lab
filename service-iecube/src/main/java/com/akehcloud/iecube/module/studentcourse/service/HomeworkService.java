package com.akehcloud.iecube.module.studentcourse.service;

import com.akehcloud.iecube.module.studentcourse.dto.HomeworkDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.qo.HomeworkQO;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkStatisticsVO;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkVO;
import com.akehcloud.iecube.module.studentcourse.vo.StudentStatisticsVO;
import com.akehcloud.model.PageTuple;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

public interface HomeworkService {

    void save(HomeworkDTO dto);

    PageTuple<HomeworkVO> query(HomeworkQO qo);

    /**
     * 打分
     *
     * @param studentLessonScheduleDTO
     */
    void score(StudentLessonScheduleDTO studentLessonScheduleDTO);

    PageTuple<HomeworkStatisticsVO> courseHomeworkStatistics(HomeworkQO qo);

    PageTuple<StudentStatisticsVO> homeworkStatistics(HomeworkQO qo);

    void zipAndDownload(Long studentId, Long courseId, HttpServletResponse response);

}
