import request from '@/utils/request'

const favoriteService = {
  cancelFavorite: (courseId) => {
    return request.get(`/student-course/cancel-favorite/${courseId}`)
  },
  favorite: (courseId) => {
    return request.get(`/student-course/favorite/${courseId}`)
  },
  list: (data) => {
    return request.post('/student-course/favorite/query', data)
  }
}

export default favoriteService
