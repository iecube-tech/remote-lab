import request from '@/utils/request'

const teacherCourseService = {
  listGrant: (query) => {
    return request.post(`/teacher-course/query`, query)
  }
}

export default teacherCourseService
