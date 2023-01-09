const commentList = [
  {
    id: 1,
    email: 'zhangsan@163.com',
    name: '张三',
    content: '通信原理ab1 ab1 ab1 ab1 ab1 ab1 ',
    courseName: '三端变阻器',
    lessonName: 'Lab1 三端变阻器',
    createTime: '2021-01-01 00:00:00',
    top: true
  },
  {
    id: 2,
    email: 'lisi@163.com',
    name: '李四',
    content: '通信原理ab1 ab1 ab1 ab1 ab1 ab1 ',
    courseName: '三端变阻器',
    lessonName: 'Lab1 三端变阻器',
    createTime: '2021-01-01 00:00:00'
  }
]

module.exports = [
  {
    url: '/comment/query/current-user',
    type: 'post',
    response: config => {
      return {
        code: 0,
        model: {
          content: commentList,
          totalCount: commentList.length
        }
      }
    }
  },
  {
    url: '/comment/top',
    type: 'put',
    response: config => {
      return {
        code: 0
      }
    }
  },
  {
    url: '/comment/[0-9]',
    type: 'get',
    response: config => {
      const id = parseInt(config.url.replace('/dev-api/comment/', ''))
      return {
        code: 0,
        model: commentList.find(o => o.id === id)
      }
    }
  },
  {
    url: '/comment',
    type: 'post',
    response: config => {
      return {
        code: 0
      }
    }
  }
]
