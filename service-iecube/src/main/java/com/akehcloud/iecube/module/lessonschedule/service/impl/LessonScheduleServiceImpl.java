package com.akehcloud.iecube.module.lessonschedule.service.impl;

import com.akehcloud.exception.runtime.UnprocessableException;
import com.akehcloud.iecube.enums.lessonschedule.SaveLessonScheduleJoinStudentEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import com.akehcloud.iecube.module.appointment.dto.AppointmentDTO;
import com.akehcloud.iecube.module.appointment.service.AppointmentService;
import com.akehcloud.iecube.module.lessonschedule.dto.DeviceOperatingDTO;
import com.akehcloud.iecube.module.lessonschedule.dto.LessonScheduleDTO;
import com.akehcloud.iecube.module.lessonschedule.entity.LessonScheduleDO;
import com.akehcloud.iecube.module.lessonschedule.mapper.LessonScheduleMapper;
import com.akehcloud.iecube.module.lessonschedule.qo.LessonScheduleQO;
import com.akehcloud.iecube.module.lessonschedule.service.LessonScheduleService;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.resource.service.ResourceService;
import com.akehcloud.iecube.module.studentcourse.dto.StudentLessonScheduleDTO;
import com.akehcloud.iecube.module.studentcourse.service.StudentLessonScheduleService;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.service.UserService;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LessonScheduleServiceImpl implements LessonScheduleService {

    @Value("${business.signal-server.url}")
    private String signalServerUrl;
    @Value("${business.signal-server.api-key}")
    private String signalServerApiKey;

    @Autowired
    private LessonScheduleMapper lessonScheduleMapper;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private StudentLessonScheduleService studentLessonScheduleService;

    @Override
    public void save(LessonScheduleDTO dto) {
        AssertUtils.allNotNull("必填参数不能为空", dto.getLessonId(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getStartDate(),
                dto.getEndDate());
        LessonScheduleDO scheduleDO = ModelUtils.convert(dto, LessonScheduleDO.class);
        lessonScheduleMapper.save(scheduleDO);
        dto.setId(scheduleDO.getId());
        if (CollectionUtils.isNotEmpty(dto.getHomeworkAttachmentKeys())) {
            lessonScheduleMapper.bindHomeworkAttachment(scheduleDO.getId(), dto.getHomeworkAttachmentKeys());
        }
        if (CollectionUtils.isNotEmpty(dto.getStudentIds())) {
            this.batchSaveStudentJoinLessonSchedule(dto.getStudentIds(), scheduleDO.getId(), SaveLessonScheduleJoinStudentEnum.SAVE);
        }
        if (CollectionUtils.isNotEmpty(dto.getDeviceList())) {
            dto.getDeviceList().forEach(item -> {
                dto.setDeviceId(item.getId());
                checkBindDeviceTime(dto, item.getDeviceName());
                Boolean exists = lessonScheduleMapper.checkExistsLessonScheduleJoinDevice(scheduleDO.getId(), item.getId());
                if (!exists) {
                    lessonScheduleMapper.bindLessonScheduleJoinDevice(scheduleDO.getId(), item);
                }
            });
            this.batchSaveAppointment(dto);
        }
        if (Objects.nonNull(dto.getAssistantId())) {
            userService.modifyAssistantIdentity(dto.getAssistantId());
        }
    }

    private void batchSaveStudentJoinLessonSchedule(List<Long> studentIds, Long lessonScheduleId, SaveLessonScheduleJoinStudentEnum saveType) {
        List<StudentLessonScheduleDTO> studentLessonScheduleList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(studentIds)) {
            studentIds.forEach(item -> {
                StudentLessonScheduleDTO studentLessonScheduleDTO = new StudentLessonScheduleDTO();
                studentLessonScheduleDTO.setStudentId(item);
                studentLessonScheduleDTO.setLessonScheduleId(lessonScheduleId);
                studentLessonScheduleDTO.setScore(0D);
                studentLessonScheduleDTO.setAppointmentCount(0);
                studentLessonScheduleList.add(studentLessonScheduleDTO);
            });
        }
        studentLessonScheduleService.batchSave(studentLessonScheduleList, saveType);
    }

    private void checkBindDeviceTime(LessonScheduleDTO dto, String deviceName) {
        LessonScheduleDTO lessonScheduleDTO = lessonScheduleMapper.checkBindDeviceTime(dto);
        if (Objects.nonNull(lessonScheduleDTO)) {
            String message = "设备：" + deviceName + " 在 " +
                    lessonScheduleDTO.getStartDate() + "至" + lessonScheduleDTO.getEndDate() + " " +
                    "已被使用,请重新选择时间";
            throw new UnprocessableException(message);
        }
    }

    private void batchSaveAppointment(LessonScheduleDTO dto) {
        LocalTime startTime = dto.getStartTime();
        LocalTime endTime = dto.getEndTime();
        Duration timeDuration = Duration.between(startTime, endTime);
        int timeCount = (int) timeDuration.toMinutes();
        LocalDate startDate = dto.getStartDate();
        LocalDate end = dto.getEndDate();
        long days = end.toEpochDay() - startDate.toEpochDay();
        LocalTime temp = startTime;
        List<AppointmentDTO> appointmentList = new ArrayList<>();
        for (DeviceOperatingDTO operatingDTO : dto.getDeviceList()) {
            for (long i = days; i >= 0; i--) {
                while ((timeCount = timeCount - dto.getAppointmentDuration()) > -1) {
                    AppointmentDTO appointmentDTO = new AppointmentDTO();
                    appointmentDTO.setLessonScheduleId(dto.getId());
                    appointmentDTO.setStatus(true);
                    appointmentDTO.setDeviceId(operatingDTO.getId());
                    LocalDate now = startDate.plusDays(i);
                    appointmentDTO.setAppointmentDate(now);
                    appointmentDTO.setStartTime(temp);
                    appointmentDTO.setEndTime(temp.plusMinutes(dto.getAppointmentDuration()));
                    temp = temp.plusMinutes(dto.getAppointmentDuration());
                    appointmentList.add(appointmentDTO);
                }
                temp = startTime;
                timeCount = (int) timeDuration.toMinutes();
            }
        }
        appointmentService.batchSave(appointmentList);
    }

    @Override
    public void modify(LessonScheduleDTO dto) {
        AssertUtils.notNull(dto.getId(), "编辑排课,id不能为空");
        lessonScheduleMapper.modify(dto);

        if (CollectionUtils.isNotEmpty(dto.getHomeworkAttachmentKeys())) {
            List<String> keys = lessonScheduleMapper.getAttachmentKeyList(dto.getId());
            if (CollectionUtils.isNotEmpty(keys)) {
                resourceService.deleteAttachmentList(keys);
            }
            lessonScheduleMapper.deleteHomeworkAttachment(dto.getId());
            lessonScheduleMapper.bindHomeworkAttachment(dto.getId(), dto.getHomeworkAttachmentKeys());
        }

        if (CollectionUtils.isEmpty(dto.getStudentIds())) {
            studentLessonScheduleService.deleteByLessonScheduleId(dto.getId());
        } else {
            this.batchSaveStudentJoinLessonSchedule(dto.getStudentIds(), dto.getId(), SaveLessonScheduleJoinStudentEnum.MODIFY);
        }

        lessonScheduleMapper.deleteDeviceList(dto.getId());
        appointmentService.deleteByLessonScheduleId(dto.getId());
        if (CollectionUtils.isNotEmpty(dto.getDeviceList())) {
            dto.getDeviceList().forEach(item -> {
                dto.setDeviceId(item.getId());
                checkBindDeviceTime(dto, item.getDeviceName());
                Boolean exists = lessonScheduleMapper.checkExistsLessonScheduleJoinDevice(dto.getId(), item.getId());
                if (!exists) {
                    lessonScheduleMapper.bindLessonScheduleJoinDevice(dto.getId(), item);
                }
            });
            this.batchSaveAppointment(dto);
        }
        if (Objects.nonNull(dto.getAssistantId())) {
            userService.modifyAssistantIdentity(dto.getAssistantId());
        }
    }

    @Override
    public LessonScheduleVO byId(Long id) {
        LessonScheduleVO vo = lessonScheduleMapper.byId(id);
        //作业附件
        List<String> keys = lessonScheduleMapper.getAttachmentKeyList(id);
        vo.setHomeworkAttachmentList(resourceService.getAttachmentList(keys));
        //学生信息
        List<Long> studentIds = lessonScheduleMapper.getStudentIds(id);
        List<UserDTO> userList = userService.getUserListByIds(studentIds);
        vo.setStudentIds(studentIds);
        vo.setStudentList(userList);
        if (vo.getAssistantId() != null) {
            UserDTO assistant = userService.get(vo.getAssistantId());
            if (assistant != null) {
                vo.setAssistantName(assistant.getName());
            }
        }
        //设备信息
        List<DeviceOperatingDTO> deviceList = lessonScheduleMapper.getDeviceList(id);
        vo.setDeviceList(deviceList);
        return vo;
    }

    @Override
    public void delete(Long id) {
        List<String> keys = lessonScheduleMapper.getAttachmentKeyList(id);
        if (CollectionUtils.isNotEmpty(keys)) {
            resourceService.deleteAttachmentList(keys);
        }
        lessonScheduleMapper.delete(id);
        lessonScheduleMapper.deleteDeviceList(id);
        lessonScheduleMapper.deleteHomeworkAttachment(id);
        studentLessonScheduleService.deleteByLessonScheduleId(id);
        appointmentService.deleteByLessonScheduleId(id);
    }

    @Override
    public DeviceOperatingDTO getDeviceOperating(Long lessonScheduleId, Long deviceId) {
        LessonScheduleVO lessonScheduleVO = lessonScheduleMapper.byId(lessonScheduleId);
        DeviceOperatingDTO deviceOperating = lessonScheduleMapper.getDeviceOperating(lessonScheduleId, deviceId);
        deviceOperating.setSignalServerUrl(signalServerUrl);
        deviceOperating.setSignalServerApiKey(signalServerApiKey);
        deviceOperating.setLessonId(lessonScheduleVO.getLessonId());
        deviceOperating.setLessonName(lessonScheduleVO.getLessonName());
        deviceOperating.setExperimentOperationPageUrl(lessonScheduleVO.getExperimentOperationPageUrl());
        return deviceOperating;
    }

    @Override
    public PageTuple<LessonScheduleDTO> query(LessonScheduleQO qo) {
        Long count = lessonScheduleMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<LessonScheduleDTO> list = lessonScheduleMapper.query(qo);
        list.forEach(item -> {
            Long studentCount = lessonScheduleMapper.studentCount(item.getId());
            item.setStudentCount(studentCount);
        });
        return PageTuple.of(count, list);
    }


}
