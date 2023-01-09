import request from '@/utils/request'

const commentService = {
  add: (data) => {
    return request.post('/comment', data)
  },
  list: (query) => {
    return request.post('/comment/student/query', query)
  }

}

export default commentService
