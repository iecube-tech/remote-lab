<template>
  <div>
    <div v-if="routerName === 'TeacherCourseDetail'" style="padding: 20px">
      <div class="detail-title">课程名称</div>
      <div class="detail-course-name">{{ detail.name }}</div>
      <div class="detail-title">课程简介</div>
      <div class="detail-course-summary">{{ detail.summary }}</div>
      <div class="detail-title">Provider</div>
      <div class="detail-course-provider">{{ detail.organizationName }}</div>
      <div class="detail-title">包含课节</div>
      <div style="margin-bottom: 36px">
        <div
          v-for="item in teacherLessonList"
          :key="'lesson' + item.id"
          class="lesson-card"
          @click="toLessonDetail(item.id)"
        >
          <span class="lesson-name">{{ item.name }}</span>
        </div>
      </div>
    </div>
    <router-view />
  </div>
</template>

<script>
import courseService from '@/service/course'
import lessonService from '@/service/lesson'

export default {
  name: 'TeacherCourseDetail',
  data() {
    return {
      routerName: undefined,
      id: undefined,
      detail: {},
      teacherLessonList: [],
      showGrantDialog: false
    }
  },
  mounted() {
    if (this.$route.name === 'TeacherCourseDetail') {
      this.routerName = this.$route.name
      this.id = parseInt(this.$route.params.teacherCourseId)
      this.fetchData()
    }
  },
  methods: {
    fetchData() {
      this.get()
      this.listTeacherLesson()
    },
    async get() {
      const { model } = await courseService.get(this.id)
      this.detail = model
    },
    async listTeacherLesson() {
      const { model } = await lessonService.listByCourseId(this.id)
      this.teacherLessonList = model
    },
    onConfirm(selected) {
      this.showGrantDialog = false
    },
    toLessonDetail(teacherLessonId) {
      this.$router.push({
        name: 'TeacherLessonDetail',
        params: {
          teacherCourseId: this.id,
          teacherLessonId: teacherLessonId
        }
      })
    }
  }
}
</script>

<style scoped>
.detail-title {
  font-size: 16px;
  color: #000000;
  line-height: 16px;
  font-weight: 600;
  margin-bottom: 24px;
}

.detail-course-name {
  font-size: 24px;
  color: #4A90E2;
  line-height: 16px;
  font-weight: 400;
  margin-top: 12px;
  margin-bottom: 56px;
}

.detail-course-summary {
  font-size: 16px;
  color: #999999;
  line-height: 16px;
  font-weight: 400;
  margin-bottom: 36px;
}

.detail-course-provider {
  font-size: 16px;
  color: #999999;
  line-height: 16px;
  font-weight: 400;
  margin-bottom: 36px;
}

.lesson-card {
  display: inline-block;
  width: 320px;
  height: 40px;
  background: #E6F1FC;
  border: 1px solid #A3D0FD;
  border-radius: 4px;
  padding: 11px;
  margin: 8px 16px 8px 0;
  cursor: pointer;
}

.lesson-name {
  font-size: 14px;
  color: #1989FA;
  letter-spacing: 0;
  font-weight: 400;
}
</style>
