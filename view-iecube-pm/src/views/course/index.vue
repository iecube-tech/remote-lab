<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>
            {{ routerTitle }}
            <el-divider v-if="courseName" direction="vertical" />
            {{ courseName }}
          </span>
          <el-button v-if="routerName === 'CourseList'" class="list-button" type="text" @click="jump('CourseAdd')">新增
          </el-button>
          <el-button v-else class="list-button" type="text" @click="goBack">返回</el-button>
          <el-button v-if="routerName === 'CourseDetail'" class="list-button button-text-warning" type="text" @click="jump('CourseModify')">编辑
          </el-button>
          <el-button v-if="routerName === 'LessonDetail'" class="list-button button-text-warning" type="text" @click="jump('LessonModify')">编辑
          </el-button>
        </div>
      </div>
      <router-view />
    </el-card>
  </div>
</template>

<script>

import courseService from '@/service/course'

export default {
  name: 'Course',
  data() {
    return {
      routerName: undefined,
      routerTitle: undefined,
      courseId: undefined,
      courseName: undefined
    }
  },
  mounted() {
    this.routerName = this.$route.name
    this.routerTitle = this.$route.meta.title
    if (this.$route.params.courseId) {
      this.courseId = this.$route.params.courseId
      this.getCourse()
    }
  },
  methods: {
    jump(routerName) {
      this.$router.push({
        name: routerName,
        params: {
          courseId: this.$route.params.courseId
        }
      })
    },
    async getCourse() {
      const { model } = await courseService.get(this.courseId)
      this.courseName = model.name
    },
    goBack() {
      this.$router.push({
        name: this.$route.meta.parentName
      })
    }
  }
}
</script>
<style scoped>
.list-button {
  float: right;
  padding: 3px 0;
  margin-left: 16px
}
</style>
