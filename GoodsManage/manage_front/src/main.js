import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import VueNativeSock from "vue-native-websocket";

import '@/icons' // icon 图标
import '@/permission' // permission control 权限控制

Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

Vue.use(VueNativeSock,'ws://localhost:8088/sensorData',{
  reconnection:true,
  reconnectionAttempts:5,
  reconnectionDelay:3000,
  format:'json'
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
