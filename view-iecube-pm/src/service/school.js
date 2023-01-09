import request from '@/utils/request'

const schoolService = {
  list: (data) => {
    return request.post('/school/query', data)
  },
  add: (data) => {
    return request.post('/school', data)
  },
  edit: (data) => {
    return request.put('/school', data)
  },
  get: (id) => {
    return request.get(`/school/${id}`)
  },
  deleteById: (id) => {
    return request.delete(`/school/${id}`)
  },
  listGrantCourse: (schoolId) => {
    return request.get(`/school/course/school-id/${schoolId}`)
  }
}

export default schoolService
