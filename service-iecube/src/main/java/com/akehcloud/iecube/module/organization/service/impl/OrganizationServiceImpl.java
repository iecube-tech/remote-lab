package com.akehcloud.iecube.module.organization.service.impl;

import com.akehcloud.iecube.enums.common.EnableStatusEnum;
import com.akehcloud.iecube.enums.user.UserTypeEnum;
import com.akehcloud.iecube.module.organization.dto.OrganizationDTO;
import com.akehcloud.iecube.module.organization.entity.OrganizationDO;
import com.akehcloud.iecube.module.organization.mapper.OrganizationMapper;
import com.akehcloud.iecube.module.organization.qo.OrganizationQO;
import com.akehcloud.iecube.module.organization.repository.OrganizationRepository;
import com.akehcloud.iecube.module.organization.service.OrganizationService;
import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.service.UserService;
import com.akehcloud.model.PageTuple;
import com.akehcloud.util.AssertUtils;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserService userService;

    @Override
    public void save(OrganizationDTO dto) {
        AssertUtils.notNull(dto.getName(), "机构名称不能为空");
        AssertUtils.notNull(dto.getAdminEmail(), "管理员邮箱不能为空");
        AssertUtils.notNull(dto.getAdminName(), "管理员姓名不能为空");
        OrganizationDO organizationDO = ModelUtils.convert(dto, OrganizationDO.class);
        organizationDO.setCreatorId(dto.getCreatorId());
        organizationDO.setCreateTime(new Date());
        organizationDO.setLastOperatorId(dto.getCreatorId());
        organizationDO.setLastOperateTime(new Date());
        organizationDO.setStatus(EnableStatusEnum.ENABLE);
        organizationRepository.save(organizationDO);
        UserDTO userDTO = new UserDTO();
        userDTO.setCreatorId(dto.getCreatorId());
        userDTO.setNum("0");
        userDTO.setName(dto.getAdminName());
        userDTO.setEmail(dto.getAdminEmail());
        userDTO.setType(UserTypeEnum.USER_ORG_ADMIN);
        userDTO.setOrganizationId(organizationDO.getId());
        UserDTO user = userService.save(userDTO);
        organizationDO.setAdminId(user.getId());
        organizationRepository.save(organizationDO);
    }

    @Override
    public void modify(OrganizationDTO dto) {
        AssertUtils.notNull(dto.getId(), "机构id不能为空");
        organizationMapper.modify(dto);
        Optional<OrganizationDO> optional = organizationRepository.findById(dto.getId());
        if (optional.isPresent()) {
            OrganizationDO organizationDO = optional.get();
            Long adminId = organizationDO.getAdminId();
            userService.updateNameAndEmail(adminId, dto.getAdminName(), dto.getAdminEmail());
        }
    }

    @Override
    public PageTuple<OrganizationDTO> query(OrganizationQO qo) {
        Long count = organizationMapper.count(qo);
        if (count == 0) {
            return PageTuple.empty();
        }
        List<OrganizationDTO> list = organizationMapper.query(qo);
        return PageTuple.of(count, list);
    }

    @Override
    public OrganizationDTO get(Long id) {
        Optional<OrganizationDO> optional = organizationRepository.findById(id);
        if (optional.isPresent()) {
            OrganizationDO organizationDO = optional.get();
            OrganizationDTO dto = ModelUtils.convert(organizationDO, OrganizationDTO.class);
            UserDTO user = userService.get(organizationDO.getAdminId());
            if (user != null) {
                dto.setAdminEmail(user.getEmail());
                dto.setAdminName(user.getName());
            }
            return dto;
        }
        return null;
    }

}
