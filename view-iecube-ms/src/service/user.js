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
  listBySchool: (query) => {
    return request.post('/user/query/school', query)
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
  updateStatus: (id, status) => {
    return request.put('/user/status', { id: id, status: status })
  },
  updateSchoolStatus: (idList, status) => {
    return request.put('/user/school-status', { idList: idList, schoolStatus: status })
  },
  downloadTemplate: () => {
    return request.get('/user/template', { responseType: 'blob' })
  },
  importByExcel: (file) => {
    const data = new FormData()
    data.append('file', file)
    return request.post('/user/batch/excel', data)
  },
  modifyPwd: (data) => {
    return request.put('/user/password', data)
  },
  resetPassword: (email) => {
    return request.put('/user/password/reset', { email: email })
  }
}

export default userService
