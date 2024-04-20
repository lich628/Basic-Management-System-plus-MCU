import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    username: '',
    password: '',
    avatar: ''
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
  SET_NAME: (state, username) => {
    state.username = username
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_PWD: (state, password) => {
    state.password = password
  }
}

const actions = {
  // 逐行解释以下代码
  // user login
  // 该方法用于用户登录
  // 该方法接收两个参数：commit和userInfo
  // commit是一个用于提交mutation的方法
  // userInfo是一个对象，包含用户的用户名和密码
  // 该方法返回一个Promise对象
  // 该方法首先调用login方法，该方法在api/user.js中定义
  // login方法接收一个对象，包含用户名和密码
  // login方法返回一个Promise对象
  // 如果login方法调用成功，执行then方法
  // then方法接收一个函数，该函数接收一个response对象
  // 该函数从response对象中解构出data对象
  // 该函数调用commit方法，提交SET_TOKEN mutation
  // SET_TOKEN mutation接收两个参数：state和token
  // state是当前模块的state对象
  // token是从data对象中解构出来的token
  // 该函数调用setToken方法，将token保存到Cookie中
  // 该函数调用resolve方法，结束Promise
  // 如果login方法调用失败，执行catch方法
  // catch方法接收一个函数，该函数接收一个error对象
  // 该函数调用reject方法，结束Promise

  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          return reject('Verification failed, please Login again.')
        }

        const { username, password, avatar } = data

        commit('SET_NAME', username)
        commit('SET_PWD', password)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
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

