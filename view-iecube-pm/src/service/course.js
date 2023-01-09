import request from '@/utils/request'

const courseService = {
  list: (query) => {
    return request.post('/course/query', query)
  },
  add: (data) => {
    return request.post('/course', data)
  },
  edit: (data) => {
    return request.put('/course', data)
  },
  get: (id) => {
    return request.get(`/course/${id}`)
  },
  deleteById: (id) => {
    return request.delete(`/course/${id}`)
  },
  grant: (idList, schoolId) => {
    return request.post('/course/grant', { courseIds: idList, schoolId: schoolId })
  },
  listAll: () => {
    return request.get('/course/all')
  }
}

export default courseService
