import request from '@/utils/request'

const commentService = {
  listByCurrentTeacher: (params) => {
    return request.post('/comment/query', params)
  },
  get: (id) => {
    return request.get(`/comment/${id}`)
  },
  deleteById: (id) => {
    return request.delete(`/comment/${id}`)
  },
  top: (id) => {
    return request.put(`/comment/top/${id}`)
  },
  reply: (data) => {
    return request.post(`/reply`, data)
  }
}

export default commentService
