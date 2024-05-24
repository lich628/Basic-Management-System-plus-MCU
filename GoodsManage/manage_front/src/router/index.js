import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import Layout from '@/layout'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '主页', icon: 'dashboard' }
      }
    ]
  },
  {
    path: '/users',
    component: Layout,
    redirect: '/users/list',
    meta: { title: '用户', icon: 'el-icon-s-custom' },
    children: [
      {
        path: 'list',
        name: 'users',
        component: () => import('@/views/users/infoList.vue'),
        meta: { title: '所有用户', icon: 'el-icon-view' }
      },
      {
        path: 'newUser',
        name: 'newUser',
        component: () => import('@/views/users/newUser.vue'),
        meta: { title: '新增用户', icon: 'el-icon-plus', roles: [0] }
      },
      {
        path: 'userInfo',
        name: 'userInfo',
        component: () => import('@/views/users/infoOwn.vue'),
        meta: { title: '个人信息', icon: 'el-icon-document' }
      },
      {
        path: 'userBatch',
        name: 'userBatch',
        component: () => import('@/views/users/userBatch.vue'),
        meta: { title: '相关批次', icon: 'el-icon-document' }
      }
    ]
  },
  {
    path: '/chart',
    component: Layout,
    redirect: '/chart/piechart',
    name: '数据查看',
    meta: {
      title: '数据查看',
      icon: 'el-icon-s-platform'
    },
    children: [
      {
        path: 'piechart',
        component: () => import('@/views/chart/index.vue'),
        name: 'Piechart',
        meta: { title: '物资概况', icon: 'el-icon-pie-chart' }
      },
      {
        path: 'linechart',
        component: () => import('@/views/chart/linechart.vue'),
        name: 'Linechart',
        meta: { title: '仓库监控', icon: 'el-icon-data-line' }
      }
    ]
  },
  {
    path: '/check',
    component: Layout,
    redirect: '/check/viewBatch',
    name: 'check',
    meta: { title: '处理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'viewBatch',
        name: 'viewBatch',
        component: () => import('@/views/goods/batchList.vue'),
        meta: { title: '暂存区', icon: 'el-icon-folder-opened' }
      }
    ]
  },
  {
    path: '/operate',
    component: Layout,
    redirect: '/operate/newBatch',
    name: 'operate',
    meta: { title: '操作', icon: 'el-icon-edit' },
    children: [
      {
        path: 'newBatch',
        name: 'newBatch',
        component: () => import('@/views/form/newBatch.vue'),
        meta: { title: '新增批次', icon: 'el-icon-plus' }
      },
      {
        path: 'opBatch',
        name: 'opBatch',
        component: () => import('@/views/form/opBatch.vue'),
        meta: { title: '操作批次', icon: 'el-icon-s-release' }
      },
      {
        path: 'newGoods',
        name: 'newGoods',
        component: () => import('@/views/goods/newGoods.vue'),
        meta: { title: '新增物资', icon: 'el-icon-plus', roles: [0] }
      },
      {
        path: 'newRecord',
        name: 'NewRecord',
        component: () => import('@/views/form/newRecord.vue'),
        meta: { title: '新增打卡', icon: 'el-icon-edit-outline' }
      }
    ]
  },
  {
    path: '/info',
    component: Layout,
    redirect: '/info/goods',
    name: 'Info',
    meta: { title: '信息管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'goods',
        name: 'Goods',
        component: () => import('@/views/goods/goodsList.vue'),
        meta: { title: '物资信息', icon: 'table' }
      },
      {
        path: 'historyBatch',
        name: 'historyBatch',
        component: () => import('@/views/info/closedBatches.vue'),
        meta: { title: '完结批次记录', icon: 'table' }
      },
      {
        path: 'orders',
        name: 'orders',
        component: () => import('@/views/info/ordersList.vue'),
        meta: { title: '采购信息', icon: 'table' }
      },
      {
        path: 'supplier',
        name: 'supplier',
        component: () => import('@/views/info/suppliers.vue'),
        meta: { title: '供应商信息', icon: 'table' }
      },
      {
        path: 'logistics',
        name: 'logistics',
        component: () => import('@/views/info/logistics.vue'),
        meta: { title: '物流信息', icon: 'table' }
      },
      {
        path: 'ioRecords',
        name: 'ioRecords',
        component: () => import('@/views/info/ioRecords.vue'),
        meta: { title: 'IO信息', icon: 'table' }
      },
      {
        path: 'record',
        name: 'Record',
        component: () => import('@/views/info/record.vue'),
        meta: { title: '打卡信息', icon: 'tree' }
      }
    ]
  },
  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'http://localhost:8088/swagger-ui.html',
        meta: { title: '项目后端API', icon: 'link' }
      }
    ]
  },
  {
    path: '*',
    component: Layout,
    children: [
      {
        path: '*',
        component: () => import('@/views/404'),
        meta: { title: '找不到界面', icon: '404' },
        hidden: true
      }
    ]
  }
]

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

router.beforeEach((to, from, next) => {
  const userRole = store.state.user.role
  for (let record of to.matched) {
    if (record.meta && record.meta.roles) {
      if (record.meta.roles.includes(userRole)) {
        next()
        return
      } else {
        next({ path: '/404' })
        return
      }
    }
  }
  next()
})

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
