package com.akehcloud.iecube.module.lessonschedule.repository;

import com.akehcloud.iecube.module.lessonschedule.entity.LessonScheduleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonScheduleRepository extends JpaRepository<LessonScheduleDO, Long> {
}
