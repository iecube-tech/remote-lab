import request from '@/utils/request'

const lessonService = {
  list: (query) => {
    return request.post('/lesson/query', query)
  },
  add: (data) => {
    return request.post('/lesson', data)
  },
  edit: (data) => {
    return request.put('/lesson', data)
  },
  byCourseId: (id) => {
    return request.get(`/lesson/course-id/${id}`)
  },
  get: (id) => {
    return request.get(`/lesson/${id}`)
  },
  deleteById: (id) => {
    return request.delete(`/lesson/${id}`)
  }
}

export default lessonService
