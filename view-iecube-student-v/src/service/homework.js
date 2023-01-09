import request from '@/utils/request'

const homeworkService = {
  list: (data) => {
    return request.post('/homework/self', data)
  },
  submit: (data) => {
    return request.post('/homework', data)
  }
}

export default homeworkService
