import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '../src/styles/element-variables.scss'
import App from './App.vue'
import router from './router'

import store from '@/store'
import '@/permission'
import * as filters from '@/filters'

Vue.config.productionTip = false
Vue.prototype.$bus = new Vue()

Vue.use(ElementUI)

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
