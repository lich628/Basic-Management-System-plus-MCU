import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'), // @指的是src路径
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '主页', icon: 'dashboard' }
    }]
  },

  {
    path: '/users',
    component: Layout,
    redirect: '/users/list',
    meta: { title: '用户', icon: 'el-icon-s-custom'},
    children: [
      {
      path: 'list',
      name: 'users',
      component: () => import('@/views/users/infoList.vue'),
      meta: { title: '所有用户', icon: 'el-icon-view' },
    },
      {
        path: 'userInfo',
        name: 'userInfo',
        component: () => import('@/views/users/infoOwn.vue'),
        meta: { title: '个人信息', icon: 'el-icon-document' },
      },
      {
        path: 'userBatch',
        name: 'userBatch',
        component: () => import('@/views/users/userBatch.vue'),
        meta: { title: '相关批次', icon: 'el-icon-document' },
      }
    ]
  },

  {
    path: '/chart',
    component: Layout,
    redirect: '/chart/piechart',
    name: '数据分析',
    meta: {
      title: '数据分析',
      icon: 'el-icon-s-platform'
    },
    children: [
      {
        path: 'piechart',
        component: () => import('@/views/chart/piechart'),
        name: 'Piechart',
        meta: { title: '物资概况', icon: 'el-icon-pie-chart' }
      },
      {
        path: 'linechart',
        component: () => import('@/views/chart/linechart'), // Parent router-view
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
      },
    ]
  },

  {
    path: '/operate',
    component: Layout,
    redirect: '/operate/newBatch',
    name: 'operate',
    meta: { title: '操作', icon: 'el-icon-edit'},
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
    meta: { title: '信息查看', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'goods',
        name: 'Goods',
        component: () => import('@/views/goods/goodsList.vue'),
        meta: { title: '物资查看', icon: 'table' }
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
        meta: { title: '物流查看', icon: 'table' }
      },
      {
        path: 'record',
        name: 'Record',
        component: () => import('@/views/info/record.vue'),
        meta: { title: '打卡记录查看', icon: 'tree' }
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

  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({

  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
