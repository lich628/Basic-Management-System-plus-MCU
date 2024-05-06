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
      meta: { title: '欢迎', icon: 'dashboard' }
    }]
  },

  {
    path: '/users',
    component: Layout,
    redirect: '/users/list',
    meta: { title: '用户管理', icon: 'el-icon-s-custom'},
    children: [
      {
      path: 'list',
      name: 'users',
      component: () => import('@/views/users/list/index'),
      meta: { title: '用户总览', icon: 'el-icon-view' },
    },
      {
        path: 'order',
        name: 'order',
        component: () => import('@/views/users/list/index'),
        meta: { title: '相关订单', icon: 'el-icon-document' },
      }
    ]
  },

  {
    path: '/query',
    component: Layout,
    redirect: '/query/goods',
    name: 'Query',
    meta: { title: '查看', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'goods',
        name: 'Goods',
        component: () => import('@/views/query/goods/index'),
        meta: { title: '物资查看', icon: 'table' }
      },
      {
        path: 'record',
        name: 'Record',
        component: () => import('@/views/query/record/index'),
        meta: { title: '打卡记录查看', icon: 'tree' }
      }
    ]
  },

  {
    path: '/batchManage',
    component: Layout,
    redirect: '/batchManage/viewBatch',
    name: 'Batch',
    meta: { title: '入库/出库 信息', icon: 'el-icon-edit'},
    children: [
      {
        path: 'viewBatch',
        name: 'viewBatch',
        component: () => import('@/views/query/batch/index'),
        meta: { title: '暂存区', icon: 'el-icon-document' }
      },
      {
        path: 'batchIn',
        name: 'batchIn',
        component: () => import('@/views/query/batch/index'),
        meta: { title: '入库', icon: 'el-icon-document' }
      },
      {
        path: 'batchOut',
        name: 'batchOut',
        component: () => import('@/views/query/batch/index'),
        meta: { title: '出库', icon: 'el-icon-document' }
      },
      {
        path: 'newRecord',
        name: 'NewRecord',
        component: () => import('@/views/form/newRecord/index'),
        meta: { title: '新增打卡', icon: 'form' }
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
