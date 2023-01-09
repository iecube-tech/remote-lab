import request from '@/utils/request'

const lessonScheduleService = {
  list: (query) => {
    return request.post(`/lesson-schedule/query`, query)
  },
  listByCurrentUser: (query) => {
    return request.post(`/lesson-schedule/current-user`, query)
  },
  get: (id) => {
    return request.get(`/lesson-schedule/${id}`)
  },
  add: (data) => {
    return request.post('/lesson-schedule', data)
  },
  modify: (data) => {
    return request.put('/lesson-schedule', data)
  },
  deleteById: (id) => {
    return request.delete(`/lesson-schedule/${id}`)
  }
}

export default lessonScheduleService
