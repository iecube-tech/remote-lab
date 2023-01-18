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
  list: (query) => {
    return request.post('/user/query', query)
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
  updateStatus: (id, status) => {
    return request.put('/user/status', { id: id, status: status })
  },
  downloadTemplate: () => {
    return request.get('/user/template')
  },
  importByExcel: (file) => {
    const data = new FormData()
    data.append('file', file)
    return request.post('/user/batch', data)
  },
  resetPassword: (email) => {
    return request.put('/user/password/reset', { email: email })
  }
}

export default userService
