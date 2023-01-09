<template>
  <div class="app-container">
    <el-card :body-style="routerName === 'LessonScheduleList' && {padding: 0}">
      <div slot="header">
        <div slot="header">
          <span>{{ routerTitle }}</span>
          <el-button
            v-if="routerName === 'LessonScheduleList'"
            class="list-button"
            type="text"
            @click="toAddOrModify('LessonScheduleAdd')"
          >新增</el-button>
          <el-button
            v-if="['LessonScheduleDetail', 'LessonScheduleAdd', 'LessonScheduleModify'].includes(routerName)"
            class="list-button"
            type="text"
            @click="goBack"
          >返回</el-button>
          <el-button
            v-if="routerName === 'LessonScheduleDetail'"
            class="list-button button-text-warning"
            type="text"
            @click="toAddOrModify('LessonScheduleModify')"
          >编辑</el-button>
        </div>
      </div>
      <router-view />
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'LessonSchedule',
  data() {
    return {
      routerName: undefined,
      routerTitle: undefined
    }
  },
  mounted() {
    this.routerName = this.$route.name
    this.routerTitle = this.$route.meta.title
  },
  methods: {
    toAddOrModify(routerName) {
      this.$router.push({
        name: routerName,
        params: {
          lessonScheduleId: this.$route.params.lessonScheduleId,
          courseId: this.$route.params.courseId
        }
      })
    },
    goBack() {
      this.$router.go(-1)
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

.list-button {
  float: right;
  padding: 3px 0;
  margin-left: 16px
}
</style>
