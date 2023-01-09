package com.akehcloud.iecube;

import com.akehcloud.iecube.module.lessonschedule.vo.LessonScheduleVO;
import com.akehcloud.iecube.module.studentcourse.qo.StudentCourseQO;
import com.akehcloud.iecube.module.studentcourse.service.StudentCourseService;
import com.akehcloud.iecube.module.studentcourse.vo.CourseDetailVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ServiceIlabApplicationTests {

    @Autowired
    private StudentCourseService studentCourseService;


    @Test
    public void lessonList() {
        StudentCourseQO qo = new StudentCourseQO();
        qo.setStudentId(43L);
        qo.setSchoolId(18L);
        qo.setCourseId(8L);
        List<LessonScheduleVO> lessonList = studentCourseService.listLessonScheduleForStudent(qo);
        System.out.println(lessonList);
    }

//    @Test
//    public void courseDetail() {
//        StudentCourseQO qo = new StudentCourseQO();
//        qo.setStudentId(43L);
//        qo.setSchoolId(18L);
//        qo.setCourseId(8L);
//        CourseDetailVO courseDetail = studentCourseService.getCourseDetail(qo);
//        System.out.println(courseDetail);
//    }

}
