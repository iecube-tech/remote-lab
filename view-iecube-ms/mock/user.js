const tokens = {
  admin: {
    token: 'admin-token'
  },
  editor: {
    token: 'editor-token'
  }
}

const users = {
  'admin-token': {
    roles: ['admin'],
    introduction: 'I am a super administrator',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Super Admin'
  },
  'editor-token': {
    roles: ['editor'],
    introduction: 'I am an editor',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Normal Editor'
  }
}

const data = [
  {
    id: 1,
    num: '10001',
    name: '张三',
    faculty: '理工学院',
    grade: '大三',
    classroom: '通信1班',
    email: 'admin@qinghua.com',
    schoolUserStatus: 'IN_SCHOOL'
  },
  {
    id: 2,
    num: '10002',
    name: '李四',
    faculty: '理工学院',
    grade: '大三',
    classroom: '通信1班',
    email: 'admin@qinghua.com',
    schoolUserStatus: 'IN_SCHOOL'
  },
  {
    id: 3,
    num: '10003',
    name: '李四',
    faculty: '理工学院',
    grade: '大三',
    classroom: '通信1班',
    email: 'admin@qinghua.com',
    schoolUserStatus: 'LEAVE_SCHOOL'
  },
  {
    id: 4,
    num: '10004',
    name: '李四',
    faculty: '理工学院',
    grade: '大三',
    classroom: '通信1班',
    email: 'admin@qinghua.com',
    schoolUserStatus: 'LEAVE_SCHOOL'
  }
]

module.exports = [
  // user login
  {
    url: '/login',
    type: 'post',
    response: config => {
      const { username } = config.body
      const token = tokens[username]
      if (!token) {
        return {
          code: 60204,
          message: 'Account and password are incorrect.'
        }
      }
      return {
        code: 20000,
        data: token
      }
    }
  },
  {
    url: '/current-user',
    type: 'get',
    response: config => {
      const info = users['admin-token']
      if (!info) {
        return {
          code: 50008,
          message: 'Login failed, unable to get user details.'
        }
      }
      return {
        code: 20000,
        data: info
      }
    }
  },
  {
    url: '/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  },
  {
    url: '/user/query',
    type: 'post',
    response: config => {
      const res = {
        code: 0,
        model: {
          content: data,
          totalCount: 4
        }
      }
      if (config.body.idIn && config.body.idIn.length > 0) {
        res.model.content = res.model.content.filter(o => config.body.idIn.includes(o.id))
        res.model.totalCount = res.model.content.length
      }
      if (config.body.schoolUserStatus) {
        res.model.content = res.model.content.filter(o => o.schoolUserStatus === config.body.schoolUserStatus)
        res.model.totalCount = res.model.content.length
      }
      return res
    }
  },
  {
    url: '/user/[0-9]',
    type: 'get',
    response: config => {
      const id = parseInt(config.url.replace('/dev-api/user/', ''))
      return {
        code: 0,
        model: data.find(o => o.id === id)
      }
    }
  },
  {
    url: '/user',
    type: 'post',
    response: config => {
      return {
        code: 0
      }
    }
  },
  {
    url: '/user',
    type: 'put',
    response: config => {
      return {
        code: 0
      }
    }
  },
  {
    url: '/user/[0-9]',
    type: 'delete',
    response: config => {
      return {
        code: 0
      }
    }
  }

]
