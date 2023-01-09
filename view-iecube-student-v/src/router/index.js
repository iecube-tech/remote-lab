import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '../views/index.vue'
import Navigation from '../components/navigation'
import PublicCourse from '../views/publicCourse.vue'
import PersonalCourse from '../views/personalCourse.vue'
import CourseCollection from '../views/courseCollection.vue'
import PersonalAppointment from '../views/personalAppointment.vue'
import PersonalHomework from '../views/personalHomework.vue'
import PersonalInfo from '../views/personalInfo.vue'
import courseDetail from '../views/courseDetail.vue'
import deviceControl from '../views/deviceControl.vue'
import LessonDetail from '../views/lessonDetail'

const originalPush = VueRouter.prototype.push

VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'index',
    component: Index
  },
  {
    path: '/',
    name: 'Navigation',
    component: Navigation,
    children: [
      {
        path: 'public_course',
        name: 'publicCourse',
        component: PublicCourse,
        meta: { title: '公开课程' }
      },
      {
        path: 'personal_course',
        name: 'personalCourse',
        component: PersonalCourse,
        meta: { title: '我的课程' }
      },
      {
        path: 'course_collection',
        name: 'courseCollection',
        component: CourseCollection,
        meta: { title: '我的收藏' }
      },
      {
        path: 'personal_appointment',
        name: 'personalAppointment',
        component: PersonalAppointment,
        meta: { title: '我的预约' }
      },
      {
        path: 'personal_homework',
        name: 'personalHomework',
        component: PersonalHomework,
        meta: { title: '我的作业' }
      },
      {
        path: 'personal_info',
        name: 'personalInfo',
        component: PersonalInfo,
        hidden: true
      },
      {
        path: 'course_detail/:courseId',
        name: 'courseDetail',
        component: courseDetail,
        hidden: true,
        children: [
          {
            path: 'lesson/:lessonId',
            name: 'LessonDetail',
            component: LessonDetail,
            hidden: true,
            meta: { active: 'publicCourse' }
          },
          {
            path: 'lesson-schedule/:lessonId/:lessonScheduleId',
            name: 'LessonScheduleDetail',
            component: LessonDetail,
            hidden: true,
            meta: { active: 'personalCourse' }
          },
          {
            path: 'lesson-schedule/:lessonId',
            name: 'LessonSchedulePublicLesson',
            component: LessonDetail,
            hidden: true,
            meta: { active: 'personalCourse' }
          }
        ]
      },
      {
        path: 'device_control/:date/:startTime/:endTime/:deviceId/:lessonScheduleId',
        name: 'deviceControl',
        component: deviceControl,
        hidden: true
      }
    ]
  },
  {
    path: '*',
    redirect: '/',
    hidden: true
  }
]

const router = new VueRouter({
  routes
})

export function resetRouter () {
  const newRouter = new VueRouter({
    routes
  })
  router.matcher = newRouter.matcher // reset router
}

export default router
