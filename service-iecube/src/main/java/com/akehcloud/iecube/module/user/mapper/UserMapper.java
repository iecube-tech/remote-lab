package com.akehcloud.iecube.module.user.mapper;

import com.akehcloud.iecube.module.user.dto.UserDTO;
import com.akehcloud.iecube.module.user.entity.UserDO;
import com.akehcloud.iecube.module.user.qo.UserQO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDTO getByEmail(String email);

    List<UserDTO> getUserListByIds(@Param("studentIds") List<Long> studentIds);

    List<UserDTO> list(UserQO qo);

    Long count(UserQO qo);

    UserDTO getById(UserQO qo);

}
