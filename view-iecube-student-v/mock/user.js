const user = {
  id: 1,
  name: '张三',
  email: 'zhangsan@123.com',
  num: '10001',
  avatarUrl: '',
  faculty: '理工学院',
  grade: '大三',
  classroom: '通信一班'
}

const token = 'tokentokentokentokentoken'

module.exports = [
  {
    url: '/login',
    type: 'post',
    response: config => {
      return {
        code: 0,
        model: token
      }
    }
  },
  {
    url: '/current-user',
    type: 'get',
    response: config => {
      return {
        code: 0,
        model: user
      }
    }
  },
  {
    url: '/logout',
    type: 'post',
    response: _ => {
      return {
        code: 0
      }
    }
  }
]
