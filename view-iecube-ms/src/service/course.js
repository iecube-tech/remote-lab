import request from '@/utils/request'

const courseService = {
  get: (id) => {
    return request.get(`/course/${id}`)
  }
}

export default courseService
