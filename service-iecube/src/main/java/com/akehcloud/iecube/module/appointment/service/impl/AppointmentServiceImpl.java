package com.akehcloud.iecube.module.appointment.service.impl;

import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.module.appointment.dto.AppointmentDTO;
import com.akehcloud.iecube.module.appointment.mapper.AppointmentMapper;
import com.akehcloud.iecube.module.appointment.qo.AppointmentQO;
import com.akehcloud.iecube.module.appointment.service.AppointmentService;
import com.akehcloud.iecube.module.appointment.vo.AppointmentVO;
import com.akehcloud.iecube.module.device.service.DeviceService;
import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.akehcloud.iecube.module.lessonschedule.mapper.LessonScheduleMapper;
import com.akehcloud.iecube.module.lessonschedule.service.LessonScheduleService;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.studentcourse.mapper.StudentLessonScheduleMapper;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private LessonScheduleMapper lessonScheduleMapper;
    @Autowired
    private StudentLessonScheduleMapper studentLessonScheduleMapper;

    @Autowired
    private LessonScheduleService lessonScheduleService;


    @Override
    public void batchSave(List<AppointmentDTO> dtoList) {
        if (CollectionUtils.isNotEmpty(dtoList)) {
            appointmentMapper.batchSave(dtoList);
        }
    }

    @Override
    public void deleteByLessonScheduleId(Long lessonScheduleId) {
        appointmentMapper.deleteByLessonScheduleId(lessonScheduleId);
    }

    @Override
    public void appointment(AppointmentDTO dto) {
        AssertUtils.notNull(dto.getDeviceId(), "预约设备id不能为空");
        AssertUtils.notNull(dto.getLessonScheduleId(), "排课id不能为空");
        AssertUtils.notNull(dto.getAppointmentDate(), "预约日期不能为空");
        AssertUtils.allNotNull("预约时间不能为空", dto.getStartTime(), dto.getEndTime());

        LessonScheduleVO lessonScheduleVO = lessonScheduleMapper.byId(dto.getLessonScheduleId());
        if (Objects.isNull(lessonScheduleVO)) {
            throw new UnprocessableException("该排课信息不存在,无法预约");
        }

        Boolean leisure = appointmentMapper.exitsAppointmentLeisure(dto);
        if (!leisure) {
            throw new UnprocessableException("该时间段预约已满,请更换时间");
        }
        Long count = appointmentMapper.countByStudentIdAndLessonScheduleId(dto.getStudentId(), dto.getLessonScheduleId());
        if (count >= lessonScheduleVO.getAppointmentCount()) {
            throw new UnprocessableException("该节课程预约次数已满");
        }
        appointmentMapper.appointment(dto);
        studentLessonScheduleMapper.addAppointmentCount(dto.getStudentId(), dto.getLessonScheduleId(), 1);
    }

    @Override
    public List<AppointmentDTO> lessonScheduleAppointmentList(AppointmentQO qo) {
        AssertUtils.notNull(qo.getLessonScheduleId(), "排课id不能为空");
        AssertUtils.notNull(qo.getDeviceId(), "设备id不能为空");
        AssertUtils.notNull(qo.getAppointmentDate(), "预约日期不能为空");
        List<AppointmentDTO> list = appointmentMapper.lessonScheduleAppointmentList(qo);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public List<AppointmentDTO> getAppointmentByStudentIdAndLessonScheduleId(Long studentId, Long lessonScheduleId) {
        List<AppointmentDTO> list = appointmentMapper.getAppointmentByStudentIdAndLessonScheduleId(studentId, lessonScheduleId);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public PageTuple<AppointmentVO> myAppointment(AppointmentQO qo) {
        Long count = appointmentMapper.myAppointmentCount(qo.getStudentId());
        if (count == 0) {
            return PageTuple.empty();
        }
        List<AppointmentVO> list = appointmentMapper.myAppointment(qo);
        /**HashSet set = new HashSet(list);
        list.clear();
        list.addAll(set);*/
        List newList = list.stream().distinct().collect(Collectors.toList());
        return PageTuple.of(count, newList);
    }

    @Override
    public void cancel(AppointmentDTO dto) {
        AssertUtils.notNull(dto.getDeviceId(), "预约设备id不能为空");
        AssertUtils.notNull(dto.getLessonScheduleId(), "排课id不能为空");
        AssertUtils.notNull(dto.getAppointmentDate(), "预约日期不能为空");
        AssertUtils.allNotNull("预约时间不能为空", dto.getStartTime(), dto.getEndTime());
        appointmentMapper.cancel(dto);
        studentLessonScheduleMapper.addAppointmentCount(dto.getStudentId(), dto.getLessonScheduleId(), -1);
    }

    @Override
    public DeviceOperatingDTO getDeviceOperating(AppointmentDTO dto) {
        AssertUtils.notNull(dto.getDeviceId(), "设备信息不能为空");
        AssertUtils.notNull(dto.getLessonScheduleId(), "排课信息不能为空");
        AppointmentDTO appointment = appointmentMapper.getAppointment(dto);
        AssertUtils.notNull(appointment, "预约不存在");
        LocalDateTime startTime = LocalDateTime.of(appointment.getAppointmentDate(), appointment.getStartTime());
        AssertUtils.state(startTime.isBefore(LocalDateTime.now()), "预约未开始");
        LocalDateTime endTime = LocalDateTime.of(appointment.getAppointmentDate(), appointment.getEndTime());
        AssertUtils.state(endTime.isAfter(LocalDateTime.now()), "预约已结束");
        return lessonScheduleService.getDeviceOperating(dto.getLessonScheduleId(), dto.getDeviceId());
    }

}
