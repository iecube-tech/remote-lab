import request from '@/utils/request'

const studentCourseService = {
  list: (data) => {
    return request.post('/student-course/self', data)
  },
  get: (courseId, lessonId) => {
    return request.get(`/student-course/self/course/detail/${courseId}`)
  },
  listLessonSchedule: (courseId) => {
    return request.get(`/student-course/lesson-schedule/course-id/${courseId}`)
  },
  getLessonSchedule: (lessonScheduleId) => {
    return request.get(`/student-course/lesson-schedule/${lessonScheduleId}`)
  }
}

export default studentCourseService
