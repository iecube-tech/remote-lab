import request from '@/utils/request'

const deviceService = {
  add: (data) => {
    return request.post(`/device`, data)
  },
  modify: (data) => {
    return request.put(`/device`, data)
  },
  list: (query) => {
    return request.post(`/device/query`, query)
  },
  get: (id) => {
    return request.get(`/device/${id}`)
  },
  deleteById: (id) => {
    return request.delete(`/device/${id}`)
  },
  getSignalServerUrl: () => {
    return request.get(`/device/signal-server-url`)
  }
}

export default deviceService
