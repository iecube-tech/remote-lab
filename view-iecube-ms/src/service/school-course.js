import request from '@/utils/request'

const schoolCourseService = {
  list: (query) => {
    return request.post('/school-course/query', query)
  },
  listGrantUser: (id) => {
    return request.get(`/school-course/grant-user/${id}`)
  },
  grant: (params) => {
    return request.post('/school-course/grant', params)
  },
  cancelGrant: (params) => {
    return request.post('/school-course/cancel-grant', params)
  }
}

export default schoolCourseService
