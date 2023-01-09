<template>
  <div>
    <div class="body-container">
      <div class="course-menu">
        <div class="course-base-info">
          <div class="tittle-info">
            <span class="course-title">{{ course.courseName }}</span>
            <span v-if="type === 'public'" style="cursor: pointer">
              <img :src="require('@/assets/images/collect.svg')" alt="collect" v-if="course.isFavorite"
                   @click="uncollectCourse">
              <img :src="require('@/assets/images/uncollect.svg')" alt="uncollect" v-else @click="collectCourse">
            </span>
          </div>
          <div class="courseSummary">
            {{ course.summary }}
          </div>
        </div>
        <div class="line"></div>
        <div class="lesson-list">
          <div class="lesson-tips">课节列表</div>
          <div v-for="lesson in lessonList" :key="'lesson' + lesson.lessonId + (lesson.lessonScheduleId || '')">
            <div class="lesson-list-container" @click="changeLesson(lesson)"
                 :style="lesson.selected ? { backgroundColor: '#E6FFFB', color: '#2DA2A6' } : {}">
              <img v-if="type === 'schedule'" :src="require('@/assets/images/' + lesson.status + '.svg')"
                   :alt="lesson.status" class="lesson-staus-icon">
              <span class="lesson-tittle">{{ lesson.lessonName }}</span>
              <span style="font-size: 10px" v-if="lesson.creatorName && type === 'schedule'"> - {{
                  lesson.creatorName
                }}</span>
            </div>
          </div>
        </div>
      </div>
      <router-view/>
    </div>
  </div>
</template>

<script>
import schoolCourseService from '../service/school-course'
import favoriteService from '../service/favorite'
import studentCourseService from '@/service/student-course'
import lessonService from '@/service/lesson'

export default {
  name: 'CourseDetail',
  data () {
    return {
      type: 'public',
      schedule: false,
      courseId: undefined,
      lessonId: undefined,
      lessonScheduleId: undefined,
      course: {},
      lessonList: []
    }
  },
  watch: {
    '$route' (to, from) {
      this.init()
      this.fetchData()
    }
  },
  mounted () {
    this.init()
    this.fetchData()
  },
  methods: {
    init () {
      this.courseId = parseInt(this.$route.params.courseId)
      this.lessonId = parseInt(this.$route.params.lessonId)
      if (this.$route.name === 'LessonDetail') {
        this.type = 'public'
        this.schedule = false
      } else if (this.$route.name === 'LessonScheduleDetail') {
        this.lessonScheduleId = parseInt(this.$route.params.lessonScheduleId)
        this.type = 'schedule'
        this.schedule = true
      } else if (this.$route.name === 'LessonSchedulePublicLesson') {
        this.lessonScheduleId = undefined
        this.type = 'schedule'
        this.schedule = false
      }
    },
    fetchData () {
      this.courseDetail()
    },
    changeLesson (lesson) {
      if (this.type === 'schedule') {
        if (lesson.lessonScheduleId) {
          this.$router.push({
            name: 'LessonScheduleDetail',
            params: {
              courseId: this.$route.params.courseId,
              lessonId: lesson.lessonId,
              lessonScheduleId: lesson.lessonScheduleId
            }
          })
        } else {
          this.$router.push({
            name: 'LessonSchedulePublicLesson',
            params: {
              courseId: this.$route.params.courseId,
              lessonId: lesson.lessonId
            }
          })
        }
      } else if (this.type === 'public') {
        this.$router.push({
          name: 'LessonDetail',
          params: {
            courseId: this.$route.params.courseId,
            lessonId: lesson.lessonId
          }
        })
      }
    },
    async courseDetail () {
      const { model: course } = await schoolCourseService.get(this.courseId)
      this.course = course
      if (this.type === 'public') {
        const { model: lessonList } = await lessonService.listByCourseId(this.courseId)
        const result = []
        for (let i = 0; i < lessonList.length; i++) {
          const lesson = lessonList[i]
          result[i] = {
            lessonId: lesson.id,
            lessonName: lesson.name
          }
        }
        this.lessonList = result
      } else if (this.type === 'schedule') {
        const { model } = await studentCourseService.listLessonSchedule(this.courseId)
        this.lessonList = model
        this.processLessonStatus()
      }
      this.processSelected()
    },
    processSelected () {
      for (let i = 0; i < this.lessonList.length; i++) {
        if (this.type === 'public') {
          if (this.lessonList[i].lessonId === this.lessonId) {
            this.$set(this.lessonList[i], 'selected', true)
          }
        } else if (this.type === 'schedule') {
          if (this.schedule) {
            if (this.lessonList[i].lessonScheduleId && this.lessonList[i].lessonScheduleId === this.lessonScheduleId) {
              this.$set(this.lessonList[i], 'selected', true)
            }
          } else {
            if (this.lessonList[i].lessonId === this.lessonId && (!this.lessonList[i].lessonScheduleId)) {
              this.$set(this.lessonList[i], 'selected', true)
            }
          }
        }
      }
    },
    processLessonStatus () {
      for (let i = 0; i < this.lessonList.length; i++) {
        const lesson = this.lessonList[i]
        if (lesson.lessonScheduleId) {
          const start = new Date(lesson.startDate + ' ' + lesson.startTime)
          const end = new Date(lesson.endDate + ' ' + lesson.endTime)
          const now = new Date()
          if (now.getTime() < start.getTime()) {
            this.$set(this.lessonList[i], 'status', 'notStarted')
          } else if (now.getTime() >= start.getTime() && now.getTime() <= end) {
            this.$set(this.lessonList[i], 'status', 'half')
          } else {
            this.$set(this.lessonList[i], 'status', 'complete')
          }
        } else {
          this.$set(this.lessonList[i], 'status', 'none')
        }
      }
    },
    async uncollectCourse () {
      const { code } = await favoriteService.cancelFavorite(this.courseId)
      if (code === 0) {
        await this.courseDetail()
      }
    },
    async collectCourse () {
      const { code } = await favoriteService.favorite(this.courseId)
      if (code === 0) {
        await this.courseDetail()
      }
    }
  }
}
</script>

<style lang="scss" scoped>

.body-container {
  margin-top: 60px;
  font-size: 14px;
  overflow-x: hidden;

  .course-menu {
    height: calc(100vh - 60px);
    width: 320px;
    background-color: #fff;
    position: fixed;
    top: 60px;

    .course-base-info {
      margin-top: 40px;
      margin-left: 20px;
      margin-right: 20px;

      .tittle-info {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .course-title {
          font-size: 16px;
          font-weight: 600;
          color: rgba(0, 0, 0, 0.85);
          line-height: 24px;
        }
      }

      .courseSummary {
        margin-top: 20px;
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.65);
        line-height: 24px;
      }
    }

    .line {
      margin-top: 20px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
    }

    .lesson-tips {
      margin-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: rgba(0, 0, 0, 0.65);
      line-height: 24px;
    }

    .lesson-list-container {
      display: flex;
      align-items: center;
      margin-top: 10px;
      width: 320px;
      height: 40px;
      color: rgba(0, 0, 0, 0.65);
      cursor: pointer;

      .lesson-staus-icon {
        margin-left: 20px;
        width: 20px;
        height: 20px;
      }

      .lesson-tittle {
        margin-left: 20px;
      }
    }

  }

  .lesson-container {
    margin-left: 340px;
    width: calc(100vw - 360px);
    position: relative;
    top: 20px;

    .lesson-baseInfo, .appointmentAndHomework, .lesson-contentInfo, .lesson-commentInfo {
      background-color: #fff;
      margin-top: 20px;
    }

    .lesson-baseInfo {
      display: flex;
      align-items: center;

      .lesson-img {
        width: 400px;
        height: 240px;
      }

      .lesson-baseInfo-container {
        margin-left: 20px;
        margin-right: 20px;

        .lesson-name {
          margin-top: 20px;
          font-size: 20px;
          font-weight: 600;
          color: #2DA2A6;
          line-height: 30px;
        }

        .lesson-summary {
          margin-top: 12px;
          font-size: 14px;
          font-weight: 400;
          color: rgba(0, 0, 0, 0.65);
          line-height: 24px;
        }

        .line {
          margin-top: 12px;
          width: 100%;
          height: 1px;
          background-color: rgba(0, 0, 0, 0.1);
          margin-bottom: 12px;
        }

        .lesson-fileName {
          font-size: 14px;
          font-weight: 400;
          color: rgba(0, 0, 0, 0.85);
          line-height: 24px;
          margin-right: 20px;
        }

        .lesson-fileDown {
          font-size: 14px;
          font-weight: 400;
          color: #2DA2A6;
          line-height: 24px;
          margin-right: 60px;
        }

        .lesson-personal {
          margin-top: 20px;

          .lesson-date {
            font-size: 14px;
            font-weight: 400;
            color: rgba(0, 0, 0, 0.85);
            line-height: 20px;
            margin-bottom: 20px;
          }

          .lesson-operation {
            margin-bottom: 20px;
          }
        }
      }

    }
  }

  .appointmentAndHomework {

    .appointmentAndHomework-tittle {
      padding-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: #2DA2A6;
      line-height: 24px;
    }

    .line {
      margin-top: 12px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
      margin-bottom: 12px;
    }

    .appointmentAndHomework-info {
      display: flex;
      justify-content: space-between;
      margin-left: 20px;
      margin-right: 20px;

      .appointmentAndHomework-deviceControl {
        margin-bottom: 40px;
      }

      .appointmentAndHomework-detailInfo {
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.85);
        line-height: 24px;
        margin-bottom: 20px;

        .appointmentAndHomework-operation {
          font-size: 14px;
          font-weight: 400;
          color: #2DA2A6;
          line-height: 24px;
          margin-left: 40px;
        }
      }

      .lesson-score {
        font-size: 48px;
        font-weight: bold;
        color: #FFA940;
        line-height: 72px;

        .lesson-score-word {
          font-size: 16px;
          line-height: 24px;
          margin-left: -12px;
        }
      }
    }
  }

  .lesson-contentInfo {

    .lesson-contentInfo-tittle {
      padding-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: #2DA2A6;
      line-height: 24px;
    }

    .line {
      margin-top: 12px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
      margin-bottom: 12px;
    }

    .lesson-contentInfo-detail {
      font-size: 14px;
      font-weight: 400;
      color: rgba(0, 0, 0, 0.85);
      line-height: 24px;
      margin: 20px;
      padding-bottom: 20px;
    }
  }

  .lesson-commentInfo {

    .lesson-commentInfo-tittle {
      padding-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: #2DA2A6;
      line-height: 24px;
    }

    .line {
      margin-top: 12px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
      margin-bottom: 12px;
    }

    .lesson-commentInfo-textarea {
      margin-left: 20px;
      margin-right: 20px;
      margin-top: 12px;

      /deep/ .el-textarea__inner {
        width: calc(100% - 40px);
        height: 80px;
      }
    }

    .lesson-commentInfo-button {
      margin-left: 20px;
      margin-bottom: 20px;
    }

    .comment-area {
      margin: 20px;

      .lesson-commentInfo-content {
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.85);
        line-height: 24px;
        margin-bottom: 12px;
      }

      .lesson-commentInfo-creatorName {
        font-size: 14px;
        font-weight: 400;
        color: #2DA2A6;
        line-height: 24px;
      }

      .lesson-commentInfo-nomalInfo {
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.65);
        line-height: 24px;
        margin-left: 40px;
      }

      .comment-top {
        right: 40px;
        position: absolute;
      }

      .lesson-replyInfo {
        background-color: #E6FFFB;
        margin-top: 8px;
        padding-left: 20px;
        padding-right: 20px;
        padding-top: 12px;
        padding-bottom: 12px;
      }

      .line {
        margin-top: 12px;
        width: 100%;
        height: 1px;
        background-color: rgba(0, 0, 0, 0.1);
        margin-bottom: 12px;
      }
    }

  }
}

.appointForm {

  .date-picker {
    width: 320px;
  }

  .time-picker {
    width: 320px;
  }

  .device-picker {
    margin-right: 30px;
  }

  .dialog-footer {
    text-align: left;
  }
}

.upload-demo {
  text-align: center;
  margin-top: 40px;
}
</style>
