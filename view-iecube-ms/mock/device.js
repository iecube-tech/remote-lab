const deviceList = [
  {
    id: 1,
    deviceId: 'sdfsds',
    name: '设备1',
    serviceUrl: 'https://www.baidu.com',
    location: '北蔡',
    liveUrl: 'https://www.baidu.com',
    status: 'ENABLE'
  },
  {
    id: 2,
    deviceId: 'sdfs3wds',
    name: '设备2',
    serviceUrl: 'https://www.baidu.com',
    location: '北蔡',
    liveUrl: 'https://www.baidu.com',
    status: 'ENABLE'
  },
  {
    id: 3,
    deviceId: 'sdfsewds',
    name: '设备3',
    serviceUrl: 'https://www.baidu.com',
    location: '北蔡',
    liveUrl: 'https://www.baidu.com',
    status: 'DISABLE'
  }
]

module.exports = [
  {
    url: '/device/query',
    type: 'post',
    response: config => {
      return {
        code: 0,
        model: {
          content: deviceList,
          totalCount: deviceList.length
        }
      }
    }
  },
  {
    url: '/device',
    type: 'post',
    response: config => {
      return {
        code: 0
      }
    }
  },
  {
    url: '/device',
    type: 'put',
    response: config => {
      return {
        code: 0
      }
    }
  },
  {
    url: '/device/[0-9]',
    type: 'get',
    response: config => {
      const id = parseInt(config.url.replace('/dev-api/device/', ''))
      return {
        code: 0,
        model: deviceList.find(o => o.id === id)
      }
    }
  }
]
