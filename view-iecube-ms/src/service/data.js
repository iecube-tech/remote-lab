import request from '@/utils/request'

const dataService = {
  listFaculty: () => {
    return request.get('/faculty')
  },
  listGrade: () => {
    return request.get('/grade')
  },
  listGradeClass: () => {
    return request.get('/grade-class')
  },
  listSchoolCourse: () => {
    return request.get('/school/course/simple/list')
  },
  listTeacherCourse: () => {
    return request.get('/teacher/course/simple/list')
  },
  listLesson: (id) => {
    return request.get(`/lesson/simple/list/${id}`)
  },
  listLessonSchedule: (id) => {
    return request.get(`/lesson-schedule/simple/list/${id}`)
  }
}

export default dataService
