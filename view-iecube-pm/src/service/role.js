import request from '@/utils/request'

const roleService = {
  listRoleBySchoolId: (schoolId) => {
    return request.get(`/role/school-id/${schoolId}`)
  },
  grant: (data) => {
    return request.post('/role/grant', data)
  }
}

export default roleService
