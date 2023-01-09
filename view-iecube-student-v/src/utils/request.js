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
      config.data.paged = true
    }
    if (config.params && config.params.currentPage && config.params.pageSize) {
      config.params.currentPage = config.params.currentPage - 1 < 0 ? 0 : config.data.currentPage - 1
      config.params.paged = true
    }
    if (store.getters.token) {
      config.headers['x-access-token'] = getToken()
      config.headers['app-code'] = 'IECUBE_STUDENT'
    }
    return config
  }
)

service.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error(error) // for debug
    if (error.response.status === 401) {
      store.dispatch('user/resetToken').then(() => {
        location.reload()
      })
    }
    return Promise.reject(error.response.data)
  }
)

export default service
