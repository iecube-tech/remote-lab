import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index'),
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
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

const asyncRouter = [
  {
    path: '/admin',
    component: Layout,
    redirect: '/admin/school',
    name: 'Admin',
    meta: { title: '后台管理', icon: 'el-icon-s-help', permission: 'ADMIN_MANAGE' },
    children: [
      {
        path: 'school',
        name: 'AdminSchool',
        component: () => import('@/views/school'),
        meta: { title: '学校管理', icon: 'table', permission: 'SCHOOL_MANAGE' }
      },
      {
        path: 'organization',
        name: 'AdminOrganization',
        component: () => import('@/views/organization'),
        meta: { title: '机构管理', icon: 'table', permission: 'ORGANIZATION_MANAGE' }
      }
    ]
  },

  {
    path: '/content',
    component: Layout,
    redirect: '/content/user',
    name: 'School',
    meta: { title: '内容平台', icon: 'el-icon-s-help', permission: 'CONTENT_PLATFORM' },
    children: [
      {
        path: 'course-editor',
        name: 'CourseEditor',
        component: () => import('@/views/org-user'),
        meta: { title: '课程编辑用户', icon: 'tree', permission: 'COURSE_EDITOR_MANAGE' }
      },
      {
        path: 'course',
        name: 'Course',
        redirect: '/content/course/list',
        component: () => import('@/views/course'),
        meta: { title: '课程管理', icon: 'table', permission: 'COURSE_MANAGE' },
        children: [
          {
            path: 'list',
            name: 'CourseList',
            hidden: true,
            component: () => import('@/views/course/CourseList'),
            meta: { title: '课程列表', icon: 'table', activeMenu: '/content/course' }
          },
          {
            path: 'add',
            name: 'CourseAdd',
            hidden: true,
            component: () => import('@/views/course/CourseAddOrModify'),
            meta: { title: '新增课程', icon: 'table', activeMenu: '/content/course', parentName: 'CourseList' }
          },
          {
            path: 'modify/:courseId',
            name: 'CourseModify',
            hidden: true,
            component: () => import('@/views/course/CourseAddOrModify'),
            meta: { title: '编辑课程', icon: 'table', activeMenu: '/content/course', parentName: 'CourseDetail' }
          },
          {
            path: 'detail/:courseId',
            name: 'CourseDetail',
            hidden: true,
            component: () => import('@/views/course/CourseDetail'),
            meta: { title: '课程详情', icon: 'table', activeMenu: '/content/course', parentName: 'CourseList' }
          },
          {
            path: ':courseId/lesson-detail/:lessonId',
            name: 'LessonDetail',
            hidden: true,
            component: () => import('@/views/course/LessonDetail'),
            meta: { title: '课节详情', icon: 'table', activeMenu: '/content/course', parentName: 'CourseDetail' }
          },
          {
            path: ':courseId/lesson-modify/:lessonId',
            name: 'LessonModify',
            hidden: true,
            component: () => import('@/views/course/LessonAddOrModify'),
            meta: { title: '编辑课节', icon: 'table', activeMenu: '/content/course', parentName: 'CourseDetail' }
          },
          {
            path: ':courseId/lesson-add',
            name: 'LessonAdd',
            hidden: true,
            component: () => import('@/views/course/LessonAddOrModify'),
            meta: { title: '新增课节', icon: 'table', activeMenu: '/content/course', parentName: 'CourseDetail' }
          }
        ]
      }
    ]
  },
  { path: '*', redirect: '/', hidden: true }
]

export function accessRouters(permissionCode) {
  return filterAsyncRoutes(asyncRouter, permissionCode)
}

function hasPermission(permissions, route) {
  if (route.meta && route.meta.permission) {
    return permissions.includes(route.meta.permission)
  } else {
    return true
  }
}

export function filterAsyncRoutes(routes, permissions) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(permissions, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, permissions)
      }
      res.push(tmp)
    }
  })
  return res
}

export default router
