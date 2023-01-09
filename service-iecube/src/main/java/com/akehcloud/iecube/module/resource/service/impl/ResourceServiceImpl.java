package com.akehcloud.iecube.module.resource.service.impl;

import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import com.akehcloud.iecube.module.resource.entity.ResourceDO;
import com.akehcloud.iecube.module.resource.mapper.ResourceMapper;
import com.akehcloud.iecube.module.resource.repository.ResourceRepository;
import com.akehcloud.iecube.module.resource.service.ResourceService;
import com.akehcloud.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public void batchSave(List<ResourceDTO> attachmentList) {
        if (CollectionUtils.isNotEmpty(attachmentList)) {
            resourceMapper.batchSave(attachmentList);
        }
    }

    @Override
    public List<ResourceDTO> getAttachmentList(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>();
        }
        return resourceMapper.getAttachmentList(keys);
    }

    @Override
    public ResourceDTO getByKey(String key) {
        if (!StringUtils.hasText(key)){
            return new ResourceDTO();
        }
        return resourceMapper.getByKey(key);
    }

    @Override
    public void deleteAttachmentList(List<String> keys) {
        if (CollectionUtils.isNotEmpty(keys)) {
            resourceMapper.deleteAttachmentList(keys);
        }
    }

    @Override
    public void save(ResourceDTO resourceDTO) {
        ResourceDO resourceDO = ModelUtils.convert(resourceDTO, ResourceDO.class);
        resourceMapper.save(resourceDO);
    }

    @Override
    public ResourceDTO get(String key) {
        ResourceDO resourceDO = resourceRepository.findByKey(key);
        return ModelUtils.convert(resourceDO, ResourceDTO.class);
    }

    @Override
    public List<ResourceDTO> listByKeyIn(List<String> keyIn) {
        List<ResourceDO> list = resourceRepository.findByKeyIn(keyIn);
        return ModelUtils.converts(list, ResourceDTO.class);
    }

}
