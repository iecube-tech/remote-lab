package com.akehcloud.iecube.module.user.controller;

import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.iecube.module.BaseController;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.dto.UserModifyDTO;
import com.akehcloud.iecube.module.user.qo.UserQO;
import com.akehcloud.iecube.module.user.service.UserService;
import com.akehcloud.iecube.util.AuthUtils;
import com.akehcloud.iecube.util.DownloadUtils;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private static final String TEMPLATE_NAME = "用户导入模板.xlsx";

    @Value("${business.user.template.path}")
    private Resource userImportTemplate;

    @Autowired
    private UserService userService;

    @PostMapping
    public void save(@RequestBody UserDTO dto) {
        dto.setCreatorId(AuthUtils.getCurrentUserId());
        dto.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        dto.setOrganizationId(AuthUtils.getCurrentUserOrganizationId());
        userService.save(dto);
    }

    @PutMapping
    public void update(@RequestBody UserDTO dto) {
        dto.setLastOperatorId(currentUserId());
        userService.modify(dto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping(value = "/{id}")
    public UserDTO get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PutMapping(value = "/status")
    public void updateStatus(@RequestBody UserDTO userDTO) {
        AssertUtils.notNull(userDTO.getId(), "id不能为空");
        AssertUtils.notNull(userDTO.getStatus(), "状态不能为空");
        userService.updateStatus(userDTO.getId(), userDTO.getStatus());
    }

    @PutMapping(value = "/school-status")
    public void updateSchoolStatus(@RequestBody UserModifyDTO userModifyDTO) {
        AssertUtils.notEmpty(userModifyDTO.getIdList(), "id不能为空");
        AssertUtils.notNull(userModifyDTO.getSchoolStatus(), "状态不能为空");
        userService.updateSchoolStatus(userModifyDTO.getIdList(), userModifyDTO.getSchoolStatus());
    }

    @PostMapping(value = "/query")
    public PageTuple<UserDTO> list(@RequestBody UserQO qo) {
        List<UserDTO> list = userService.list(qo);
        Long totalCount = userService.count(qo);
        return PageTuple.of(totalCount, list);
    }

    @PostMapping(value = "/query/school")
    public PageTuple<UserDTO> listBySchool(@RequestBody UserQO qo) {
        qo.setSchoolId(AuthUtils.getCurrentUserSchoolId());
        List<UserDTO> list = userService.list(qo);
        Long totalCount = userService.count(qo);
        return PageTuple.of(totalCount, list);
    }

    @PostMapping(value = "/query/organization")
    public PageTuple<UserDTO> listByOrganization(@RequestBody UserQO qo) {
        qo.setOrganizationId(AuthUtils.getCurrentUserOrganizationId());
        List<UserDTO> list = userService.list(qo);
        Long totalCount = userService.count(qo);
        return PageTuple.of(totalCount, list);
    }

    @GetMapping(value = "/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            DownloadUtils.httpDownload(this.userImportTemplate.getInputStream(), TEMPLATE_NAME, response);
        } catch (IOException e) {
            log.error("IO异常", e);
            throw new SystemException();
        }
    }

    @PostMapping(value = "/batch/excel")
    public void importByExcel(MultipartFile file) {
        try {
            userService.importByExcel(file.getInputStream(), AuthUtils.getCurrentUserId(), AuthUtils.getCurrentUserSchoolId());
        } catch (IOException e) {
            log.error("IO异常", e);
            throw new SystemException();
        }
    }

    @PutMapping(value = "/password")
    public void modifyPwd(@RequestBody UserDTO userDTO) {
        AssertUtils.allNotNull("参数错误", userDTO.getOldPwd(), userDTO.getNewPwd());
        userDTO.setId(AuthUtils.getCurrentUserId());
        userService.modifyPwd(userDTO);
    }
}
