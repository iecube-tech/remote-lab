package com.akehcloud.iecube.module.resource.service;

import com.akehcloud.iecube.module.resource.dto.ResourceDTO;

import java.util.List;

public interface ResourceService {

    void batchSave(List<ResourceDTO> attachmentList);

    List<ResourceDTO> getAttachmentList(List<String> keys);

    void deleteAttachmentList(List<String> keys);

    /**
     * 保存资源
     *
     * @param resourceDTO
     * @return
     */
    void save(ResourceDTO resourceDTO);

    /**
     * 根据id获取资源模型
     *
     * @param id
     * @return
     */
    ResourceDTO get(String id);

    List<ResourceDTO> listByKeyIn(List<String> keyIn);

    ResourceDTO getByKey(String key);
}
