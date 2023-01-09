import request from '@/utils/request'

const organizationService = {
  list: (query) => {
    return request.post('/organization/query', query)
  },
  add: (data) => {
    return request.post('/organization', data)
  },
  edit: (data) => {
    return request.put('/organization', data)
  },
  get: (id) => {
    return request.get(`/organization/${id}`)
  },
  deleteById: (id) => {
    return request.delete(`/organization/${id}`)
  }
}

export default organizationService
