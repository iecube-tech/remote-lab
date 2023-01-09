package com.akehcloud.iecube.module.resource.mapper;

import com.akehcloud.iecube.module.resource.dto.ResourceDTO;
import com.akehcloud.iecube.module.resource.entity.ResourceDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceMapper {

    List<ResourceDTO> getAttachmentList(@Param("keys") List<String> keys);

    void deleteAttachmentList(@Param("keys") List<String> keys);

    @Insert(" INSERT INTO resource VALUE (#{key}, #{filename}, #{originFilename}) ")
    void save(ResourceDO resourceDO);

    void batchSave(List<ResourceDTO> attachmentList);

    ResourceDTO getByKey(String key);
}
