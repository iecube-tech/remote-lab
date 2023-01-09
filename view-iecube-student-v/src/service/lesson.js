import request from '@/utils/request'

const lessonService = {
  list: (query) => {
    return request.post('/lesson/query', query)
  },
  listByCourseId: (id) => {
    return request.get(`/lesson/course-id/${id}`)
  },
  get: (id) => {
    return request.get(`/lesson/${id}`)
  }
}

export default lessonService
