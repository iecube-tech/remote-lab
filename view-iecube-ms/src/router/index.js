import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
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
    path: '/user',
    component: Layout,
    redirect: '/user/in-school',
    name: 'User',
    meta: { title: '人员管理', icon: 'el-icon-s-help', permission: 'USER_MANAGE' },
    children: [
      {
        path: 'in-school',
        name: 'UserInSchool',
        component: () => import('@/views/user'),
        meta: { title: '在校人员管理', icon: 'table', permission: 'IN_SCHOOL_USER_MANAGE' }
      },
      {
        path: 'leave-school',
        name: 'UserLeaveSchool',
        component: () => import('@/views/user/LeaveSchoolUser'),
        meta: { title: '离校人员管理', icon: 'table', permission: 'LEAVE_SCHOOL_USER_MANAGE' }
      }
    ]
  },

  {
    path: '/school-course',
    component: Layout,
    redirect: '/school-course/grant/list',
    name: 'SchoolCourse',
    meta: { title: '课程授权', icon: 'el-icon-s-help', permission: 'SCHOOL_COURSE_MANAGE' },
    children: [
      {
        path: 'grant',
        name: 'SchoolCourseGrant',
        component: () => import('@/views/school-course'),
        redirect: '/school-course/grant/list',
        hidden: true,
        meta: { icon: 'table', activeMenu: '/school-course', breadcrumb: false },
        children: [
          {
            path: 'list',
            name: 'SchoolCourseList',
            hidden: true,
            component: () => import('@/views/school-course/SchoolCourseList'),
            meta: { title: '课程列表', icon: 'table', activeMenu: '/school-course' }
          },
          {
            path: 'detail/:schoolCourseId',
            name: 'SchoolCourseDetail',
            hidden: true,
            component: () => import('@/views/school-course/SchoolCourseDetail'),
            meta: { title: '课程详情', icon: 'table', activeMenu: '/school-course' },
            children: [
              {
                path: ':schoolLessonId',
                name: 'SchoolLessonDetail',
                hidden: true,
                component: () => import('@/views/school-course/SchoolLessonDetail'),
                meta: { title: '课节详情', icon: 'table', activeMenu: '/business/school-course' }
              }
            ]
          }
        ]
      }
    ]
  },

  {
    path: '/business',
    component: Layout,
    redirect: '/business/teacher-course',
    name: 'Business',
    meta: { title: '业务平台', icon: 'el-icon-s-help', permission: 'BUSINESS_MANAGE' },
    children: [
      {
        path: 'teacher-course',
        name: 'TeacherCourse',
        component: () => import('@/views/teacher-course'),
        meta: { title: '我的课程', icon: 'table', permission: 'TEACHER_COURSE_MANAGE' },
        children: [
          {
            path: ':teacherCourseId',
            name: 'TeacherCourseDetail',
            hidden: true,
            component: () => import('@/views/teacher-course/TeacherCourseDetail'),
            meta: { title: '课程详情', icon: 'table', activeMenu: '/business/teacher-course' },
            children: [
              {
                path: ':teacherLessonId',
                name: 'TeacherLessonDetail',
                hidden: true,
                component: () => import('@/views/teacher-course/TeacherLessonDetail'),
                meta: { title: '课节详情', icon: 'table', activeMenu: '/business/teacher-course' }
              }
            ]
          }
        ]
      },
      {
        path: 'lesson-schedule',
        name: 'LessonSchedule',
        hidden: true,
        showChildren: true,
        component: () => import('@/views/lesson-schedule'),
        meta: { title: '排课管理', icon: 'table', permission: 'LESSON_SCHEDULE_MANAGE' },
        children: [
          {
            path: 'list',
            name: 'LessonScheduleList',
            component: () => import('@/views/lesson-schedule/LessonScheduleList'),
            meta: { title: '我的排课', icon: 'table' }
          },
          {
            path: 'all',
            name: 'LessonScheduleAll',
            component: () => import('@/views/lesson-schedule/LessonScheduleAllList'),
            meta: { title: '所有排课', icon: 'table' }
          },
          {
            path: 'detail/:lessonScheduleId',
            name: 'LessonScheduleDetail',
            hidden: true,
            component: () => import('@/views/lesson-schedule/LessonScheduleDetail'),
            meta: { title: '排课详情', icon: 'table', activeMenu: '/business/lesson-schedule' }
          },
          {
            path: 'add',
            name: 'LessonScheduleAdd',
            hidden: true,
            component: () => import('@/views/lesson-schedule/LessonScheduleAddOrModify'),
            meta: { title: '新增排课', icon: 'table', activeMenu: '/business/lesson-schedule' }
          },
          {
            path: 'modify/:lessonScheduleId',
            name: 'LessonScheduleModify',
            hidden: true,
            component: () => import('@/views/lesson-schedule/LessonScheduleAddOrModify'),
            meta: { title: '编辑排课', icon: 'table', activeMenu: '/business/lesson-schedule' }
          }
        ]
      },
      {
        path: 'homework',
        name: 'Homework',
        redirect: '/business/homework/list',
        component: () => import('@/views/homework'),
        meta: { title: '作业管理', icon: 'table', permission: 'HOMEWORK_MANAGE' },
        children: [
          {
            path: 'list',
            name: 'HomeworkList',
            hidden: true,
            component: () => import('@/views/homework/HomeworkList'),
            meta: { title: '作业管理', icon: 'table', activeMenu: '/business/homework' }
          },
          {
            path: 'statistics',
            name: 'HomeworkStatistics',
            hidden: true,
            component: () => import('@/views/homework/HomeworkStatistics'),
            meta: { title: '作业统计', icon: 'table', activeMenu: '/business/homework' }
          },
          {
            path: 'course-detail',
            name: 'HomeworkCourseDetail',
            hidden: true,
            component: () => import('@/views/homework/HomeworkCourseDetail'),
            meta: { title: '课程作业统计', icon: 'table', activeMenu: '/business/homework' }
          }
        ]
      },
      {
        path: 'comment',
        name: 'Comment',
        component: () => import('@/views/comment'),
        meta: { title: '留言管理', icon: 'table', permission: 'COMMENT_MANAGE' }
      },
      {
        path: 'device',
        name: 'Device',
        component: () => import('@/views/device'),
        meta: { title: '设备管理', icon: 'table', permission: 'DEVICE_MANAGE' }
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
