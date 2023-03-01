import request from '@/utils/request'

const commentService = {
  add: (data) => {
    return request.post('/comment', data)
  },
  list: (query) => {
    return request.post('/comment/student/query', query)
  },
  sendemail: (query) => {
    return request.post('/comment/problem', query)
  }

}

export default commentService
