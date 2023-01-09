package com.akehcloud.iecube.module.lesson.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lesson.qo.LessonQO;
import com.akehcloud.iecube.module.lesson.service.LessonService;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/lesson")
public class LessonController extends BaseController {

    @Autowired
    private LessonService lessonService;

    @PostMapping
    public void
    save(@RequestBody LessonDTO dto){
        dto.setCreatorId(currentUserId());
        lessonService.save(dto);
    }

    @PutMapping
    public void modify(@RequestBody LessonDTO dto){
        dto.setLastOperatorId(currentUserId());
        lessonService.modify(dto);
    }

    @GetMapping("/{id}")
    public LessonDTO byId(@PathVariable Long id){
        return lessonService.byId(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        lessonService.delete(id);
    }

    @PostMapping("/query")
    public PageTuple<LessonDTO> query(@RequestBody LessonQO qo){
        return lessonService.query(qo);
    }

    @GetMapping("/course-id/{courseId}")
    public List<LessonDTO> getLessonListByCourseId(@PathVariable Long courseId){
        return lessonService.getLessonListByCourseId(courseId);
    }

}
