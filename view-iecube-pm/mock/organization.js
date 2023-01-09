module.exports = [
  // user login
  {
    url: '/organization/query',
    type: 'post',
    response: () => {
      return {
        code: 0,
        model: [
          {
            id: 1,
            name: '清华机构',
            adminName: '超管',
            adminEmail: 'admin@qinghua.com',
            status: 'ENABLE'
          },
          {
            id: 2,
            name: '北京机构',
            adminName: '超管',
            adminEmail: 'admin@beida.com',
            status: 'DISABLE'
          },
          {
            id: 3,
            name: '浙江机构',
            adminName: '超管',
            adminEmail: 'dmin@beida.com',
            status: 'ENABLE'
          }
        ]
      }
    }
  }
]
