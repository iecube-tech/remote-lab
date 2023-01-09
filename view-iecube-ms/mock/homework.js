const homeworkList = [
  {
    id: 1,
    studentId: 1,
    studentNum: '100001',
    studentName: '张三',
    studentFaculty: '理工学院',
    studentGrade: '大一',
    studentClassroom: '通信1班',
    courseId: 1,
    courseName: '通信原理',
    lessonId: 1,
    lessonName: 'Lab1 三端变阻器',
    homeworkAttachment: {
      key: 'https://www.baidu.com',
      filename: '作业.pdf'
    },
    submitTime: '2021-01-01',
    lessonTeacherId: 1,
    lessonTeacherName: '华罗庚',
    score: 100
  },
  {
    id: 2,
    studentId: 2,
    studentNum: '100001',
    studentName: '张三',
    studentFaculty: '理工学院',
    studentGrade: '大一',
    studentClassroom: '通信1班',
    courseId: 1,
    courseName: '通信原理',
    lessonId: 1,
    lessonName: 'Lab1 三端变阻器',
    homeworkAttachment: {
      key: 'https://www.baidu.com',
      filename: '作业.pdf'
    },
    submitTime: '2021-01-01',
    lessonTeacherId: 1,
    lessonTeacherName: '华罗庚',
    score: 100
  },
  {
    id: 3,
    studentId: 3,
    studentNum: '100002',
    studentName: '李四',
    studentFaculty: '理工学院',
    studentGrade: '大一',
    studentClassroom: '通信1班',
    courseId: 1,
    courseName: '通信原理',
    lessonId: 1,
    lessonName: 'Lab1 三端变阻器',
    homeworkAttachment: {
      key: 'https://www.baidu.com',
      filename: '作业.pdf'
    },
    submitTime: '2021-01-01',
    lessonTeacherId: 1,
    lessonTeacherName: '华罗庚',
    score: 100
  },
  {
    id: 4,
    studentId: 4,
    studentNum: '100003',
    studentName: '王五',
    studentFaculty: '理工学院',
    studentGrade: '大一',
    studentClassroom: '通信1班',
    courseId: 1,
    courseName: '通信原理',
    lessonId: 1,
    lessonName: 'Lab1 三端变阻器',
    homeworkAttachment: {
      key: 'https://www.baidu.com',
      filename: '作业.pdf'
    },
    submitTime: '2021-01-01',
    lessonTeacherId: 1,
    lessonTeacherName: '华罗庚',
    score: 100
  }
]

const homeworkStatistics = [
  {
    courseId: 1,
    courseName: '通信原理',
    receiveHomeworkCount: 60
  },
  {
    courseId: 2,
    courseName: '通信原理',
    receiveHomeworkCount: 60
  },
  {
    courseId: 3,
    courseName: '通信原理',
    receiveHomeworkCount: 60
  }
]

const courseHomeworkDetail = [
  {
    studentId: 1,
    studentName: '张三',
    studentNum: '10001',
    studentFaculty: '理工学院',
    studentGrade: '大三',
    studentClassroom: '通信1班',
    totalScore: 1000,
    totalScoreMultiWeight: 800,
    lessonHomeworkList: [
      {
        lessonId: 1,
        score: 100,
        weight: 90
      },
      {
        lessonId: 2,
        score: 100,
        weight: 90
      },
      {
        lessonId: 3,
        score: 100,
        weight: 90
      }
    ]
  },
  {
    studentId: 2,
    studentName: '李四',
    studentNum: '10002',
    studentFaculty: '理工学院',
    studentGrade: '大三',
    studentClassroom: '通信1班',
    totalScore: 1000,
    totalScoreMultiWeight: 800,
    lessonHomeworkList: [
      {
        lessonId: 1,
        score: 100,
        weight: 90
      },
      {
        lessonId: 2,
        score: 100,
        weight: 90
      },
      {
        lessonId: 3,
        score: 100,
        weight: 90
      }
    ]
  }
]

module.exports = [
  {
    url: '/homework/query/course-detail',
    type: 'post',
    response: config => {
      return {
        code: 0,
        model: {
          content: courseHomeworkDetail,
          totalCount: courseHomeworkDetail.length
        }
      }
    }
  },
  {
    url: '/homework/query',
    type: 'post',
    response: config => {
      return {
        code: 0,
        model: {
          content: homeworkList,
          totalCount: homeworkList.length
        }
      }
    }
  },
  {
    url: '/homework/statistics',
    type: 'get',
    response: config => {
      return {
        code: 0,
        model: homeworkStatistics
      }
    }
  }
]
