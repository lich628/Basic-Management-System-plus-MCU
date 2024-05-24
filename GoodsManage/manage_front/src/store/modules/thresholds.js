// src/store/modules/thresholds.js
const state = {
  temperature: 50,
  humidity: 70,
  smokeSensor: 400
}

const mutations = {
  SET_THRESHOLDS: (state, thresholds) => {
    state.temperature = thresholds.temperature;
    state.humidity = thresholds.humidity;
    state.smokeSensor = thresholds.smokeSensor;
  }
}

const actions = {
  setThresholds({ commit }, thresholds) {
    commit('SET_THRESHOLDS', thresholds)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
