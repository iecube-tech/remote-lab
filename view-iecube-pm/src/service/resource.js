import request from '@/utils/request'

const resourceService = {
  upload: (file) => {
    const data = new FormData()
    data.append('file', file)
    return request.post('/resource', data)
  }
}

export default resourceService
