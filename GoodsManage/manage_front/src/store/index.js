import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'

Vue.use(Vuex)

// 1. 创建一个新的 Vuex.Store 实例，传入一个对象
// 2. 对象中包含了三个属性：modules、getters
// 3. modules 是一个对象，包含了 app、settings、user 三个属性
// 4. app、settings、user 分别是三个模块的 store
// 5. getters 是一个对象，包含了 getter 函数
// 6. getter 函数是一个计算属性，用于获取 store 中的数据
// 7. 最后导出 store
// 8. 该 store 会在 main.js 中被引入并挂载到 Vue 实例上
// 9. 之后在组件中可以通过 this.$store 访问 store 中的数据
// 10. 也可以通过 this.$store.commit() 方法调用 mutations 中的方法
// 11. 通过 this.$store.dispatch() 方法调用 actions 中的方法

const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user
  },
  getters
})

export default store
