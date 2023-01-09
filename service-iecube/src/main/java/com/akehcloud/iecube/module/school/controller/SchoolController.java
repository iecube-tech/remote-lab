package com.akehcloud.iecube.module.school.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.course.dto.SimpleCourseDTO;
import com.akehcloud.iecube.module.school.dto.SchoolDTO;
import com.akehcloud.iecube.module.school.qo.SchoolQO;
import com.akehcloud.iecube.module.school.service.SchoolService;
import com.akehcloud.iecube.module.schoolcourse.service.SchoolCourseService;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/school")
public class SchoolController extends BaseController {

    @Autowired
    private SchoolService schoolService;

    @PostMapping
    public void save(@RequestBody SchoolDTO dto) {
        dto.setCreatorId(currentUserId());
        schoolService.save(dto);
    }

    @PutMapping
    public void modify(@RequestBody SchoolDTO dto) {
        dto.setLastOperatorId(currentUserId());
        schoolService.modify(dto);
    }

    @PostMapping("/query")
    public PageTuple<SchoolDTO> query(@RequestBody SchoolQO qo) {
        List<SchoolDTO> list = schoolService.list(qo);
        Long count = schoolService.count(qo);
        return PageTuple.of(count, list);
    }

    @GetMapping(value = "/course/school-id/{schoolId}")
    public List<SimpleCourseDTO> listGrantCourse(@PathVariable Long schoolId) {
        return schoolService.listGrantCourse(schoolId);
    }

    @GetMapping(value = "/{id}")
    public SchoolDTO get(@PathVariable Long id) {
        return schoolService.get(id);
    }

}
