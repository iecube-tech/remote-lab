package com.akehcloud.iecube.module.lesson.repository;

import com.akehcloud.iecube.module.lesson.entity.LessonDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<LessonDO, Long> {
}
