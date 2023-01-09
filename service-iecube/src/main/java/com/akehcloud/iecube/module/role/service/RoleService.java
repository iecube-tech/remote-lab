package com.akehcloud.iecube.module.role.service;

import com.akehcloud.iecube.module.role.dto.RoleDTO;
import com.akehcloud.iecube.module.role.dto.RoleGrantDTO;

import java.util.List;

public interface RoleService {

    void saveOrUpdate(Long userId, String roleCode);

    void saveOrUpdate(Long userId, List<String> roleCodeList);

    void bindRole(Long userId, String roleCode);

    void bindRole(Long userId, List<String> roleCodeList);

    void deleteJoin(Long userId);

    void batchSave(List<RoleDTO> roleList, Long schoolId);

    List<RoleDTO> listBySchoolId(Long schoolId);

    void grant(RoleGrantDTO roleGant);

}
