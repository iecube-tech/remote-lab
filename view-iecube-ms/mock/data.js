module.exports = [
  // user login
  {
    url: '/faculty',
    type: 'get',
    response: config => {
      return {
        code: 0,
        model: ['理工学院', '文商学院', '经济学院']

      }
    }
  },
  {
    url: '/grade',
    type: 'get',
    response: config => {
      return {
        code: 0,
        model: ['大一', '大二', '大三']
      }
    }
  },
  {
    url: '/classroom',
    type: 'get',
    response: config => {
      return {
        code: 0,
        model: ['通信1班', '通信2班']
      }
    }
  }
]
