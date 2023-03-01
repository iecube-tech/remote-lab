package com.akehcloud.iecube.module.comment.service.impl;

import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.iecube.email.EmailSender;
import com.akehcloud.iecube.module.comment.dto.CommentDTO;
import com.akehcloud.iecube.module.comment.dto.ReplyDTO;
import com.akehcloud.iecube.module.comment.entity.CommentDO;
import com.akehcloud.iecube.module.comment.entity.ReplyDO;
import com.akehcloud.iecube.module.comment.mapper.CommentMapper;
import com.akehcloud.iecube.module.comment.qo.CommentQO;
import com.akehcloud.iecube.module.comment.repository.CommentRepository;
import com.akehcloud.iecube.module.comment.repository.ReplyRepository;
import com.akehcloud.iecube.module.comment.service.CommentService;
import com.akehcloud.iecube.module.lesson.mapper.LessonMapper;
import com.akehcloud.iecube.module.lesson.dto.LessonDTO;
import com.akehcloud.iecube.module.lessonschedule.dto.LessonScheduleDTO;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.mapper.UserMapper;
import com.akehcloud.iecube.module.user.qo.UserQO;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private CommentRepository commentRepository;
    private static final String EMAIL_SUBJECT = "IECubeOnline 学生问题求助";
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private UserMapper userMapper;
    @Value("${email.template.student-problems}")
    private Resource studentProblems;
    @Autowired
    private EmailSender emailSender;

    @Override
    public void save(CommentDTO dto) {
        AssertUtils.notNull(dto.getSchoolId(), "学校id不能为空");
        AssertUtils.notNull(dto.getLessonId(), "课节id不能为空");
        AssertUtils.notNull(dto.getContent(), "回复内容不能为空");
        dto.setTop(false);
        CommentDO commentDO = ModelUtils.convert(dto, CommentDO.class);
        commentMapper.save(commentDO);
    }

    @Override
    public PageTuple<CommentDTO> query(CommentQO qo) {
        Long count = commentMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<CommentDTO> list = commentMapper.query(qo);
        return PageTuple.of(count, list);
    }

    @Override
    public CommentDTO detail(Long id) {
        return commentMapper.detailById(id);
    }

    @Override
    public void delete(Long id) {
        commentMapper.deleteById(id);
        replyRepository.deleteByCommentId(id);
    }

    @Override
    public void update(CommentDTO commentDTO) {
        commentMapper.update(commentDTO);
    }

    @Override
    public void top(Long id) {
        Optional<CommentDO> optional = commentRepository.findById(id);
        if (optional.isPresent()) {
            CommentDO commentDO = optional.get();
            commentDO.setTop(!commentDO.getTop());
            commentRepository.save(commentDO);
        } else {
            throw new SystemException("该留言不存在");
        }
    }

    private String buildText(Resource resource, Object... params) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            FileCopyUtils.copy(resource.getInputStream(), out);
        } catch (IOException e) {
            log.error("IO异常", e);
        }
        String text = out.toString();
        text = MessageFormat.format(text, params);
        return text;
    }

    public void sendEmail(CommentDTO dto){
        // 获取教师的邮箱  通过lessonScheduleID 查询lessonSchedule表中的creatorID
        // 通过lessonSchedule.creatorID 获取usrInfo表中的email
        // 通过CommentDTO.creatorId 获取usr_info 添加到邮件模板中然后发送邮件
        CommentQO qo = new CommentQO();
        qo.setContent(dto.getContent());
        qo.setCreatorId(dto.getCreatorId());
        qo.setLessonId(dto.getLessonId());
        qo.setLessonScheduleID(dto.getLessonScheduleID());
        LessonScheduleDTO lessonScheduleDTO = commentMapper.LessonScheduleDetail(qo);
        LessonDTO lesson = lessonMapper.getLessonById(lessonScheduleDTO.getLessonId());
        UserQO teacherQO = new UserQO();
        UserQO studentQO = new UserQO();
        teacherQO.setId(lessonScheduleDTO.getCreatorId());
        studentQO.setId(qo.getCreatorId());
        UserDTO teacher = userMapper.getById(teacherQO);
        UserDTO student = userMapper.getById(studentQO);
        String text = this.buildText(studentProblems, teacher.getName(), student.getFaculty(),student.getGrade(), student.getGradeClass(),student.getName(),lesson.getName(),qo.getContent(), student.getEmail());
        emailSender.send(teacher.getEmail(), EMAIL_SUBJECT, text);
        this.save(dto);
    }
}
