import request from '@/utils/request'

const permissionService = {
  listAll: () => {
    return request.get('/permission/all')
  }
}

export default permissionService
