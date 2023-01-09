import request from '@/utils/request'

const deviceService = {
  get: (id) => {
    return request.get(`/device/${id}`)
  }
}

export default deviceService
