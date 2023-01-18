import request from '@/utils/request'

const userService = {
  login: (data) => {
    return request.post('/login', data)
  },
  logout: () => {
    return request.delete('/logout')
  },
  currentUserInfo: () => {
    return request.get('/current-user')
  },
  listByOrganization: (query) => {
    return request.post('/user/query/organization', query)
  },
  get: (id) => {
    return request.get(`/user/${id}`)
  },
  add: (data) => {
    return request.post('/user', data)
  },
  deleteById: (id) => {
    return request.delete(`/user/${id}`)
  },
  modify: (data) => {
    return request.put('/user', data)
  },
  modifyPwd: (data) => {
    return request.put('/user/password', data)
  },
  resetPassword: (email) => {
    return request.put('/user/password/reset', { email: email })
  }
}

export default userService
