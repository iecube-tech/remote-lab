import { accessRouters, constantRoutes } from '@/router'

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, permissions) {
    return new Promise(resolve => {
      const routers = accessRouters(permissions)
      commit('SET_ROUTES', routers)
      resolve(routers)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
