import request from '@/utils/request'

const lessonService = {
  listByCourseId: (schoolCourseId) => {
    return request.get(`/lesson/course-id/${schoolCourseId}`)
  },
  get: (id) => {
    return request.get(`/lesson/${id}`)
  }
}

export default lessonService
