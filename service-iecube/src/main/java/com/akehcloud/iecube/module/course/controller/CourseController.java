package com.akehcloud.iecube.module.course.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.course.dto.CourseDTO;
import com.akehcloud.iecube.module.course.dto.GrantDTO;
import com.akehcloud.iecube.module.course.qo.CourseQO;
import com.akehcloud.iecube.module.course.service.CourseService;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.AbstractDynamicQO;
import com.akehcloud.model.PageTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/course")
public class CourseController extends BaseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public void save(@RequestBody CourseDTO dto) {
        dto.setCreatorId(currentUserId());
        dto.setOrgId(AuthUtils.getCurrentUserOrganizationId());
        courseService.save(dto);
    }

    @PutMapping
    public void modify(@RequestBody CourseDTO dto) {
        courseService.modify(dto);
    }

    @GetMapping("/{id}")
    public CourseDTO byId(@PathVariable Long id) {
        return courseService.byId(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @PostMapping("/query")
    public PageTuple<CourseDTO> query(@RequestBody CourseQO qo) {
        qo.setOrganizationId(AuthUtils.getCurrentUserOrganizationId());
        return courseService.query(qo);
    }

    @GetMapping("/all")
    public PageTuple<CourseDTO> listAll() {
        CourseQO qo = new CourseQO();
        qo.withoutPaging();
        return courseService.query(qo);
    }

    @PostMapping("/query/organization")
    public PageTuple<CourseDTO> listByOrganization(@RequestBody CourseQO qo) {
        qo.setOrganizationId(AuthUtils.getCurrentUserOrganizationId());
        return courseService.query(qo);
    }

    @PostMapping("/grant")
    public void grant(@RequestBody GrantDTO dto) {
        dto.setCreatorId(currentUserId());
        courseService.grant(dto);
    }

}
