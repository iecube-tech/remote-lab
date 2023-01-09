package com.akehcloud.iecube.module.school.mapper;

import com.akehcloud.iecube.module.school.dto.SchoolDTO;
import com.akehcloud.iecube.module.school.entity.SchoolDO;
import com.akehcloud.iecube.module.school.qo.SchoolQO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SchoolMapper {

    void save(SchoolDO schoolDO);

    void modify(SchoolDTO dto);

    List<SchoolDTO> list(SchoolQO qo);

    Long count(SchoolQO qo);

}
