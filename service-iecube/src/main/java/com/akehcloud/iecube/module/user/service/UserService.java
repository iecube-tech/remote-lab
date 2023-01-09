package com.akehcloud.iecube.module.user.service;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import com.akehcloud.iecube.enums.user.UserSchoolStatusEnum;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.qo.UserQO;

import java.io.InputStream;
import java.util.List;

public interface UserService {

    UserDTO get(Long id);

    UserDTO save(UserDTO dto);

    void modify(UserDTO dto);

    UserDTO getByEmail(String email);

    List<UserDTO> getUserListByIds(List<Long> studentIds);

    List<UserDTO> list(UserQO qo);

    Long count(UserQO qo);

    void delete(Long id);

    void updateStatus(Long id, EnableStatusEnum status);

    void updateSchoolStatus(List<Long> idList, UserSchoolStatusEnum schoolStatus);

    void modifyPwd(UserDTO userDTO);

    void importByExcel(InputStream in, Long currentUserId, Long currentUserSchoolId);

    void updateNameAndEmail(Long adminId, String adminName, String adminEmail);

    void modifyAssistantIdentity(Long id);

}
