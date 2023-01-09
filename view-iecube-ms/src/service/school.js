import request from '@/utils/request'

const schoolService = {
  list: (data) => {
    return request.post('/login', data)
  },
  add: () => {
    return request.delete('/logout')
  },
  edit: () => {
    return request.get('/current-user')
  }
}

export default schoolService
