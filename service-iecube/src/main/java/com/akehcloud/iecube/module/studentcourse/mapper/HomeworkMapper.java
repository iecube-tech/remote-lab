package com.akehcloud.iecube.module.studentcourse.mapper;

import com.akehcloud.iecube.module.studentcourse.dto.LessonScheduleStatisticsDTO;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.qo.HomeworkQO;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkStatisticsVO;
import com.akehcloud.iecube.module.studentcourse.vo.HomeworkVO;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomeworkMapper {

    Boolean existByLessonScheduleIdAndStudentId(@Param("lessonScheduleId") Long lessonScheduleId, @Param("studentId") Long studentId);

    List<HomeworkVO> query(HomeworkQO qo);

    Long count(HomeworkQO qo);

    void score(StudentLessonScheduleDTO studentLessonScheduleDTO);

    List<HomeworkStatisticsVO> homeworkStatisticsList(HomeworkQO qo);

    Long homeworkStatisticsCount(HomeworkQO qo);

    Long getStudentHomeworkCount(HomeworkQO qo);

    List<UserDTO> getStudentList(HomeworkQO qo);

    List<LessonScheduleStatisticsDTO> getScoreDetails(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    void update(@Param("key") String key, @Param("studentId") Long studentId, @Param("lessonScheduleId") Long lessonScheduleId);

}
