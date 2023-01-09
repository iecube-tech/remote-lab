package com.akehcloud.iecube.module.role.mapper;

import com.akehcloud.iecube.module.role.dto.RoleGrantDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Delete(" DELETE FROM user_join_role WHERE user_id = #{userId} ")
    void deleteJoin(Long userId);

    void batchSaveJoin(@Param("userId") Long userId, @Param("roleCodeList") List<String> roleCodeList);

    void grant(RoleGrantDTO roleGant);

    void revoke(Long schoolId);

}
