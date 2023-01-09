package com.akehcloud.iecube.module.comment.controller;

import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.comment.dto.CommentDTO;
import com.akehcloud.iecube.module.comment.qo.CommentQO;
import com.akehcloud.iecube.module.comment.service.CommentService;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public void save(@RequestBody CommentDTO dto){
        dto.setCreatorId(currentUserId());
        dto.setSchoolId(currentSchoolId());
        commentService.save(dto);
    }

    @PostMapping(value = "/query")
    public PageTuple<CommentDTO> query(@RequestBody CommentQO qo) {
        qo.setSchoolId(currentSchoolId());
        qo.setTeacherId(AuthUtils.getCurrentUserId());
        return commentService.query(qo);
    }

    @PostMapping(value = "/student/query")
    public PageTuple<CommentDTO> studentQuery(@RequestBody CommentQO qo) {
        qo.setSchoolId(currentSchoolId());
        return commentService.query(qo);
    }

    @GetMapping(value = "/{id}")
    public CommentDTO detail(@PathVariable("id") Long id) {
        AssertUtils.notNull(id, "参数错误");
        return commentService.detail(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        AssertUtils.notNull(id, "参数错误");
        commentService.delete(id);
    }

    @PutMapping
    public void update(@RequestBody CommentDTO commentDTO) {
        AssertUtils.notNull(commentDTO.getId(), "参数错误");
        commentService.update(commentDTO);
    }

    @PutMapping(value = "/top/{id}")
    public void top(@PathVariable("id") Long id) {
        AssertUtils.notNull(id, "参数错误");
        commentService.top(id);
    }
}
