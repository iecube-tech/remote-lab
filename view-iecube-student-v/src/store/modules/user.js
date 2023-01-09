import userService from '@/service/user'
import { getToken, removeToken, setToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    schoolLogoUrl: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_SCHOOL_LOGO_URL: (state, schoolLogoUrl) => {
    state.schoolLogoUrl = schoolLogoUrl
  }
}

const actions = {
  login ({ commit }, userInfo) {
    const {
      email,
      password
    } = userInfo
    return new Promise((resolve, reject) => {
      userService.login({
        email: email.trim(),
        password: password
      }).then(response => {
        const { model } = response
        commit('SET_TOKEN', model.accessToken)
        setToken(model.accessToken)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getInfo ({
    commit,
    state
  }) {
    return new Promise((resolve, reject) => {
      userService.currentUserInfo().then(response => {
        const { model } = response
        const {
          name,
          avatarUrl,
          schoolLogoUrl
        } = model
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatarUrl)
        commit('SET_SCHOOL_LOGO_URL', schoolLogoUrl)
        resolve(model)
      }).catch(error => {
        reject(error)
      })
    })
  },
  logout ({
    commit,
    state
  }) {
    return new Promise((resolve, reject) => {
      userService.logout().then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  resetToken ({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
