import request from '@/utils/request'

const schoolCourseService = {
  list: (data) => {
    return request.post('/school-course/query', data)
  },
  get: (courseId) => {
    return request.get(`/student-course/course/detail/${courseId}`)
  }
}

export default schoolCourseService
