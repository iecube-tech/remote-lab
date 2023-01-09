package com.akehcloud.iecube.module.permission.service.impl;

import com.akehcloud.iecube.module.permission.dto.PermissionDTO;
import com.akehcloud.iecube.module.permission.entity.PermissionDO;
import com.akehcloud.iecube.module.permission.mapper.PermissionMapper;
import com.akehcloud.iecube.module.permission.repository.PermissionRepository;
import com.akehcloud.iecube.module.permission.service.PermissionService;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionDTO> listAll() {
        PermissionDO example = new PermissionDO();
        example.setAppCode("IECUBE_MS");
        List<PermissionDO> list = permissionRepository.findAll(Example.of(example), Sort.by(Sort.Order.asc("sort")));
        return ModelUtils.converts(list, PermissionDTO.class);
    }

    @Override
    public List<String> listCode(String roleCode, Long schoolId) {
        return permissionMapper.listCode(roleCode, schoolId);
    }

    @Override
    public List<String> listCodeByUser(Long userId, Long schoolId) {
        return permissionMapper.listCodeByUser(userId, schoolId);
    }

}
