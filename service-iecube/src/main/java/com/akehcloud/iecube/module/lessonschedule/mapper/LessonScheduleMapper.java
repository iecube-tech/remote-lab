package com.akehcloud.iecube.module.lessonschedule.mapper;

import com.akehcloud.iecube.module.lesson.dto.SimpleLessonDTO;
import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.akehcloud.iecube.module.lessonschedule.dto.LessonScheduleDTO;
import com.akehcloud.iecube.module.lessonschedule.entity.LessonScheduleDO;
import com.akehcloud.iecube.module.lessonschedule.qo.LessonScheduleQO;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LessonScheduleMapper {

    void save(LessonScheduleDO scheduleDO);

    void modify(LessonScheduleDTO dto);

    void delete(Long id);

    void deleteHomeworkAttachment(Long id);

    void bindHomeworkAttachment(@Param("id") Long id, @Param("homeworkAttachmentKeys") List<String> homeworkAttachmentKeys);

    List<String> getAttachmentKeyList(Long id);

    LessonScheduleVO byId(Long id);

    Long studentCount(Long id);

    void bindLessonScheduleJoinDevice(@Param("lessonScheduleId") Long id, @Param("device") DeviceOperatingDTO device);

    void deleteDeviceList(Long id);

    Long count(LessonScheduleQO qo);

    List<LessonScheduleDTO> query(LessonScheduleQO qo);

    List<Long> getStudentIds(Long id);

    List<DeviceOperatingDTO> getDeviceList(Long id);

    Boolean existByIdAndCreatorId(@Param("creatorId") Long creatorId, @Param("id") Long id);

    List<SimpleLessonDTO> getSimpleLessonScheduleList(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);

    DeviceOperatingDTO getDeviceOperating(@Param("id") Long id, @Param("deviceId") Long deviceId);

    LessonScheduleDTO checkBindDeviceTime(LessonScheduleDTO dto);

    Boolean lessonScheduleTimeExpire(Long lessonScheduleId);

    Boolean checkExistsLessonScheduleJoinDevice(@Param("lessonScheduleId") Long lessonScheduleId, @Param("deviceId") Long deviceId);
}
