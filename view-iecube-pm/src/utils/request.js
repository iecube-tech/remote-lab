import axios from 'axios'
import store from '@/store'
import { getToken } from '@/utils/auth'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 5000
})

service.interceptors.request.use(
  config => {
    if (config.data && config.data.currentPage && config.data.pageSize) {
      config.data.currentPage = config.data.currentPage - 1 < 0 ? 0 : config.data.currentPage - 1
      config.data.pageMode = 1
    }
    if (config.params && config.params.currentPage && config.params.pageSize) {
      config.params.currentPage = config.params.currentPage - 1 < 0 ? 0 : config.data.currentPage - 1
      config.params.pageMode = 1
    }
    if (store.getters.token) {
      config.headers['x-access-token'] = getToken()
      config.headers['app-code'] = 'IECUBE_PM'
    }
    return config
  }
)

service.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response.status === 401) {
      if (error.response.data.code !== 10003) {
        store.dispatch('user/resetToken').then(() => {
          location.reload()
        })
      }
    }
    return Promise.reject(error.response.data)
  }
)

export default service
