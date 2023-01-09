import request from '@/utils/request'

const appointmentService = {
  list: (data) => {
    return request.post('/appointment/my-appointment', data)
  },
  appointment: (data) => {
    return request.post('/appointment', data)
  },
  listTimePeriod: (query) => {
    return request.post('/appointment/query/lesson-schedule', query)
  },
  getDeviceOperating: (data) => {
    return request.post('/appointment/device/operating', data)
  },
  cancel: (data) => {
    return request.post('/appointment/cancel', data)
  }
}

export default appointmentService
