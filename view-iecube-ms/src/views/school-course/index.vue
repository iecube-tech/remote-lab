<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>{{ routerTitle }}</span>
          <el-button v-if="routerName === 'SchoolCourseDetail'" class="list-button" type="text" @click="goBack('SchoolCourseGrant')">返回</el-button>
          <el-button v-if="routerName === 'SchoolLessonDetail'" class="list-button" type="text" @click="goBack('SchoolCourseDetail')">返回</el-button>
        </div>
      </div>
      <router-view />

    </el-card>
  </div>
</template>

<script>

export default {
  name: 'SchoolCourse',
  data() {
    return {
      data: [],
      showDetail: false,
      currentSchoolCourseId: undefined,
      loading: false,
      routerName: undefined,
      routerTitle: undefined
    }
  },
  mounted() {
    this.routerName = this.$route.name
    this.routerTitle = this.$route.meta.title
  },
  methods: {
    toDetail(id) {
      this.$router.push({
        name: 'SchoolCourseDetail',
        params: { schoolCourseId: id }
      })
    },
    goBack(routerName) {
      this.$router.push({
        name: routerName,
        params: {
          schoolCourseId: this.$route.params.schoolCourseId
        }
      })
    }
  }
}
</script>
<style scoped>
.page-wrapper {
  margin-top: 12px;
  text-align: center;
}

.card-title {
  width: 96px;
  height: 36px;
  font-size: 24px;
  font-weight: 600;
  line-height: 36px;
}

.course-name {
  font-size: 16px;
  color: #606266;
  letter-spacing: 0;
  line-height: 16px;
  font-weight: 400;
  margin-bottom: 16px;
  margin-top: 4px;
}

.course-summary {
  font-size: 14px;
  color: #909399;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 400;
  margin-bottom: 16px;
}

.course-provider {
  font-size: 16px;
  color: #606266;
  letter-spacing: 0;
  line-height: 16px;
  font-weight: 400;
}

.list-button {
  float: right;
  padding: 3px 0;
  margin-left: 16px
}
</style>
