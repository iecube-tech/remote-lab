package com.akehcloud.iecube.module.user.dao;

import com.akehcloud.iecube.module.user.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 持久层接口
 *
 * @author wangyaxing
 * @date 2021-05-20
 */
@Repository
public interface UserDAO extends JpaRepository<UserDO, Long> {
    @Query(nativeQuery = true,value = "SELECT DISTINCT faculty FROM user_info WHERE school_id = ?1 AND faculty IS NOT NULL")
    List<String> findDistinctFaculty(Long schoolId);

    @Query(nativeQuery = true,value = "SELECT DISTINCT grade FROM user_info WHERE school_id = ?1 AND grade IS NOT NULL")
    List<String> findDistinctGrade(Long schoolId);

    @Query(nativeQuery = true,value = "SELECT DISTINCT grade_class FROM user_info WHERE school_id = ?1 AND grade_class IS NOT NULL")
    List<String> findDistinctGradeClass(Long schoolId);
}
