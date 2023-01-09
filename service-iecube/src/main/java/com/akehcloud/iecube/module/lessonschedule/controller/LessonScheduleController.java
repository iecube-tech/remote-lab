package com.akehcloud.iecube.module.lessonschedule.controller;


import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.lessonschedule.dto.LessonScheduleDTO;
import com.akehcloud.iecube.module.lessonschedule.qo.LessonScheduleQO;
import com.akehcloud.iecube.module.lessonschedule.service.LessonScheduleService;
import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/lesson-schedule")
public class LessonScheduleController extends BaseController {


    @Autowired
    private LessonScheduleService lessonScheduleService;

    @PostMapping
    public void save(@RequestBody LessonScheduleDTO dto){
        dto.setCreatorId(AuthUtils.getCurrentUserId());
        dto.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        lessonScheduleService.save(dto);
    }

    @PutMapping
    public void modify(@RequestBody LessonScheduleDTO dto){
        dto.setLastOperatorId(AuthUtils.getCurrentUserId());
        lessonScheduleService.modify(dto);
    }

    @GetMapping("/{id}")
    public LessonScheduleVO byId(@PathVariable Long id){
        return lessonScheduleService.byId(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        lessonScheduleService.delete(id);
    }

    @PostMapping("/query")
    public PageTuple<LessonScheduleDTO> query(@RequestBody LessonScheduleQO qo){
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        return lessonScheduleService.query(qo);
    }

    @PostMapping("/current-user")
    public PageTuple<LessonScheduleDTO> currentUserLessonSchedule(@RequestBody LessonScheduleQO qo){
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        qo.setTeacherId(AuthUtils.getCurrentUserId());
        return lessonScheduleService.query(qo);
    }

}
