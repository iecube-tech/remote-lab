const state = {
  route: undefined
}

const mutations = {
  ROUTER_CHANGE: (state, toRoute) => {
    state.route = toRoute
  }
}

const actions = {
  routeChange ({ commit }) {
    commit('ROUTER_CHANGE')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
