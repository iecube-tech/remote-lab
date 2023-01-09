package com.akehcloud.iecube.module.role.service.impl;

import com.akehcloud.iecube.module.permission.service.PermissionService;
import com.akehcloud.iecube.module.role.dto.RoleDTO;
import com.akehcloud.iecube.module.role.dto.RoleGrantDTO;
import com.akehcloud.iecube.module.role.entity.RoleDO;
import com.akehcloud.iecube.module.role.entity.RoleEmbeddedId;
import com.akehcloud.iecube.module.role.mapper.RoleMapper;
import com.akehcloud.iecube.module.role.repository.RoleRepository;
import com.akehcloud.iecube.module.role.service.RoleService;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionService permissionService;

    @Override
    public void saveOrUpdate(Long userId, String roleCode) {
        this.saveOrUpdate(userId, new ArrayList<>(Collections.singletonList(roleCode)));
    }

    @Override
    public void saveOrUpdate(Long userId, List<String> roleCodeList) {
        this.deleteJoin(userId);
        if (CollectionUtils.isNotEmpty(roleCodeList)) {
            roleMapper.batchSaveJoin(userId, roleCodeList);
        }
    }

    @Override
    public void bindRole(Long userId, String roleCode) {
        this.bindRole(userId, new ArrayList<>(Collections.singleton(roleCode)));
    }

    @Override
    public void bindRole(Long userId, List<String> roleCodeList) {
        AssertUtils.notNull(userId, "用户ID不能为空");
        if (CollectionUtils.isNotEmpty(roleCodeList)) {
            roleMapper.batchSaveJoin(userId, roleCodeList);
        }
    }

    @Override
    public void deleteJoin(Long userId) {
        roleMapper.deleteJoin(userId);
    }

    @Override
    public void batchSave(List<RoleDTO> roleList, Long schoolId) {
        if (CollectionUtils.isNotEmpty(roleList)) {
            List<RoleDO> roleDOList = new ArrayList<>();
            List<RoleDTO> grantRoleList = new ArrayList<>();
            for (RoleDTO roleDTO : roleList) {
                RoleEmbeddedId id = new RoleEmbeddedId();
                id.setCode(roleDTO.getCode());
                id.setSchoolId(schoolId);
                Optional<RoleDO> optional = roleRepository.findById(id);
                if (!optional.isPresent()) {
                    RoleDO roleDO = ModelUtils.convert(roleDTO, RoleDO.class);
                    roleDO.setId(id);
                    roleDOList.add(roleDO);
                }
                if (CollectionUtils.isNotEmpty(roleDTO.getPermissionCodeList())) {
                    grantRoleList.add(roleDTO);
                }
            }
            if (CollectionUtils.isNotEmpty(roleDOList)) {
                roleRepository.saveAll(roleDOList);
            }
            if (CollectionUtils.isNotEmpty(grantRoleList)) {
                RoleGrantDTO roleGrantDTO = new RoleGrantDTO();
                roleGrantDTO.setSchoolId(schoolId);
                roleGrantDTO.setRoleList(grantRoleList);
                this.grant(roleGrantDTO);
            }
        }
    }

    @Override
    public List<RoleDTO> listBySchoolId(Long schoolId) {
        List<RoleDO> roleDOList = roleRepository.findByIdSchoolId(schoolId);
        List<RoleDTO> roleDTOList = new ArrayList<>();
        for (RoleDO roleDO : roleDOList) {
            RoleDTO roleDTO = ModelUtils.convert(roleDO, RoleDTO.class);
            roleDTO.setCode(roleDO.getId().getCode());
            roleDTO.setSchoolId(roleDO.getId().getSchoolId());
            List<String> permissionCodeList = permissionService.listCode(roleDO.getId().getCode(), roleDO.getId().getSchoolId());
            roleDTO.setPermissionCodeList(permissionCodeList);
            roleDTOList.add(roleDTO);
        }
        return roleDTOList;
    }

    @Override
    public void grant(RoleGrantDTO roleGant) {
        roleMapper.revoke(roleGant.getSchoolId());
        if (CollectionUtils.isNotEmpty(roleGant.getRoleList())) {
            roleMapper.grant(roleGant);
        }
    }

}
