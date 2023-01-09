package com.akehcloud.iecube.module.permission.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {

    List<String> listCode(@Param("roleCode") String roleCode, @Param("schoolId") Long schoolId);

    List<String> listCodeByUser(@Param("userId") Long userId, @Param("schoolId") Long schoolId);

}
