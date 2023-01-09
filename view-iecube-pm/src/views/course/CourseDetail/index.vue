<template>
  <div>
    <div style="padding: 20px">
      <div class="detail-title">课程名称</div>
      <div class="detail-course-name">{{ detail.name }}</div>
      <div class="detail-title">课程简介</div>
      <div class="detail-course-summary">{{ detail.summary }}</div>
      <div class="detail-title">Provider</div>
      <div class="detail-course-provider">{{ detail.organizationName }}</div>
      <div class="detail-title">包含课节</div>
      <div style="margin-bottom: 36px">
        <div
          v-for="item in lessonList"
          :key="'schoolLesson' + item.id"
          class="lesson-card"
        >
          <span class="lesson-name" @click="toLessonDetail(item.id)">{{ item.name }}</span>
          <i class="el-icon-close delete-icon" @click="delLesson(item.id)" />
        </div>
      </div>
      <el-button type="text" @click="jump('LessonAdd')">新增课节</el-button>
    </div>
  </div>
</template>

<script>
import courseService from '@/service/course'
import lessonService from '@/service/lesson'

export default {
  name: 'CourseDetail',
  data() {
    return {
      routerName: undefined,
      id: undefined,
      detail: {},
      lessonList: []
    }
  },
  mounted() {
    this.routerName = this.$route.name
    this.id = parseInt(this.$route.params.courseId)
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.get()
      this.listLesson()
    },
    async get() {
      const { model } = await courseService.get(this.id)
      this.detail = model
    },
    async listLesson() {
      const { model } = await lessonService.byCourseId(this.id)
      this.lessonList = model
    },
    toLessonDetail(lessonId) {
      this.$router.push({
        name: 'LessonDetail',
        params: {
          courseId: this.id,
          lessonId: lessonId
        }
      })
    },
    async delLesson(lessonId) {
      this.$confirm(
        '确认删除该课节吗？',
        '确认提示'
      ).then(async() => {
        await lessonService.deleteById(lessonId)
        this.fetchData()
      }).catch(e => {
        if (e === 'cancel') {
          this.$message.info('操作取消')
        } else {
          this.$message.error(e.message)
        }
      })
    },
    jump(routerName) {
      this.$router.push({
        name: routerName,
        params: {
          courseId: this.$route.params.courseId
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
}

.lesson-name {
  font-size: 14px;
  color: #1989FA;
  letter-spacing: 0;
  font-weight: 400;
  cursor: pointer;
}

.lesson-name:hover {
  color: #66b1ff;
}

.delete-icon {
  float: right;
  cursor: pointer;
  color: #1989FA;
}

.delete-icon:hover {
  color: #F56C6C;
}
</style>
