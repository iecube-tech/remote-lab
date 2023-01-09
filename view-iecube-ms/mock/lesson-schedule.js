const lessonScheduleList = [
  {
    id: 1,
    teacherName: '张三',
    teacherCourseName: '通信原理',
    teacherLessonName: 'Lab1 三端电阻器',
    startDate: '11-03-2017',
    endDate: '11-03-2017',
    studentCount: 40,
    status: 'IN_PROGRESS'
  },
  {
    id: 2,
    teacherName: '李四',
    teacherCourseName: '通信原理',
    teacherLessonName: 'Lab2 三端电阻器',
    startDate: '11-03-2017',
    endDate: '11-03-2017',
    studentCount: 40,
    status: 'IN_PROGRESS'
  },
  {
    id: 3,
    teacherName: '李四',
    teacherCourseName: '通信原理',
    teacherLessonName: 'Lab3 三端电阻器',
    startDate: '11-03-2017',
    endDate: '11-03-2017',
    studentCount: 40,
    status: 'FINISHED'
  },
  {
    id: 4,
    teacherName: '王五',
    teacherCourseName: '通信原理',
    teacherLessonName: 'Lab4 三端电阻器',
    startDate: '11-03-2017',
    endDate: '11-03-2017',
    studentCount: 40,
    status: 'FINISHED'
  }
]

const lessonSchedule = {
  id: 1,
  courseId: 1,
  courseName: '通信原理',
  lessonId: 1,
  lessonName: 'Lab1 三端电阻器',
  startDate: '2021-01-01',
  endDate: '2021-01-31',
  startTime: '09:00',
  endTime: '17:00',
  appointmentDuration: 20,
  appointmentCount: 3,
  homeworkRequire: '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈',
  weight: 80,
  homeworkAttachmentList: [
    {
      key: 'https://www.baidu.com',
      filename: '基于ELVIS III的解决方案.ppt'
    },
    {
      key: 'https://www.baidu.com',
      filename: '基于ELVIS III的解决方案.ppt'
    },
    {
      key: 'https://www.baidu.com',
      filename: '基于ELVIS III的解决方案.ppt'
    }
  ]
}

module.exports = [
  {
    url: '/lesson-schedule/query/current-user',
    type: 'post',
    response: config => {
      const res = {
        code: 0,
        model: {
          content: lessonScheduleList,
          totalCount: lessonScheduleList.length
        }
      }
      if (config.body.status) {
        res.model.content = lessonScheduleList.filter(o => o.status === config.body.status)
        res.model.totalCount = res.model.content.length
      }
      return res
    }
  },
  {
    url: '/lesson-schedule/query',
    type: 'post',
    response: config => {
      return {
        code: 0,
        model: {
          content: lessonScheduleList,
          totalCount: lessonScheduleList.length
        }
      }
    }
  },
  {
    url: '/lesson-schedule',
    type: 'post',
    response: config => {
      return {
        code: 0
      }
    }
  },
  {
    url: '/lesson-schedule/[0-9]',
    type: 'get',
    response: config => {
      const id = parseInt(config.url.replace('/dev-api/lesson-schedule/', ''))
      return {
        code: 0,
        model: lessonSchedule
      }
    }
  }
]
