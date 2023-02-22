package com.akehcloud.iecube.module.appointment.mapper;

import com.akehcloud.iecube.module.appointment.dto.AppointmentDTO;
import com.akehcloud.iecube.module.appointment.entity.AppointmentDO;
import com.akehcloud.iecube.module.appointment.enums.AppointmentStatusEnum;
import com.akehcloud.iecube.module.appointment.qo.AppointmentQO;
import com.akehcloud.iecube.module.appointment.vo.AppointmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppointmentMapper {

    void batchSave(@Param("dtoList") List<AppointmentDTO> dtoList);

    void deleteByLessonScheduleId(Long lessonScheduleId);

    void appointment(AppointmentDTO dto);

    List<AppointmentDTO> lessonScheduleAppointmentList(AppointmentQO qo);

    List<AppointmentDTO> lessonScheduleAppointmentListCanappoint(AppointmentQO qo);

    List<AppointmentDTO> getAppointmentByStudentIdAndLessonScheduleId(@Param("studentId") Long studentId, @Param("lessonScheduleId") Long lessonScheduleId);

    Long myAppointmentCount(Long studentId);

    List<AppointmentVO> myAppointment(AppointmentQO qo);

    void cancel(AppointmentDTO dto);

    Boolean exitsAppointmentLeisure(AppointmentDTO dto);

    AppointmentDTO getAppointment(AppointmentDTO dto);

    Long countByStudentIdAndLessonScheduleId(@Param("studentId") Long studentId, @Param("lessonScheduleId") Long lessonScheduleId);
}
