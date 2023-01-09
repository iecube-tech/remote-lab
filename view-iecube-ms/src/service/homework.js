import request from '@/utils/request'

const homeworkService = {
  list: (query) => {
    return request.post(`/homework/query`, query)
  },
  rating: (params) => {
    return request.put(`/homework/score`, params)
  },
  packagingDownload: (studentId, courseId) => {
    return request.get(`/homework/packaging/${studentId}/${courseId}`, { responseType: 'blob' })
  },
  statistics: (query) => {
    return request.post('/homework/course/statistics', query)
  },
  courseDetail: (query) => {
    return request.post('/homework/statistics', query)
  }
}

export default homeworkService
