<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>{{ routerTitle }}</span>
          <el-button v-if="routerName === 'TeacherCourseDetail'" class="list-button" type="text" @click="goBack('TeacherCourse')">返回</el-button>
          <el-button v-if="routerName === 'TeacherLessonDetail'" class="list-button" type="text" @click="goBack('TeacherCourseDetail')">返回</el-button>
          <el-button v-if="routerName === 'TeacherLessonDetail'" class="list-button" type="text" @click="toAddLessonSchedule()">新增排课</el-button>
        </div>
      </div>

      <div v-if="routerName === 'TeacherCourse'">
        <el-row :gutter="24">
          <el-col v-for="item in data" :key="'teacher-course' + item.id" :span="8">
            <div style="cursor: pointer;" @click="toDetail(item.id)">
              <el-card style="width: 100%; height: 160px; margin-bottom: 20px;" @click="toDetail(item.id)">
                <el-row>
                  <el-col :span="9">
                    <img :src="'local-resource/' + item.coverUrl" height="120px" width="120px" :alt="item.name">
                  </el-col>
                  <el-col :span="15">
                    <div class="course-name">{{ item.name }}</div>
                    <div class="course-summary">{{ item.summary }}</div>
                  </el-col>
                </el-row>
              </el-card>
            </div>
          </el-col>
        </el-row>
      </div>
      <router-view  />

    </el-card>
  </div>
</template>

<script>

import teacherCourseService from '@/service/teacher-course'

export default {
  name: 'TeacherCourseList',
  data() {
    return {
      data: [],
      showDetail: false,
      currentTeacherCourseId: undefined,
      loading: false,
      routerName: undefined,
      routerTitle: undefined
    }
  },
  mounted() {
    this.routerName = this.$route.name
    this.routerTitle = this.$route.meta.title
    if (this.routerName === 'TeacherCourse') {
      this.fetchData()
    }
  },
  methods: {
    fetchData() {
      this.list()
    },
    async list() {
      const { model } = await teacherCourseService.listGrant({ pageMode: 0 })
      this.data = model.content
    },
    toDetail(teacherCourseId) {
      this.currentTeacherCourseId = teacherCourseId
      this.$router.push({
        name: 'TeacherCourseDetail',
        params: { teacherCourseId: teacherCourseId }
      })
    },
    goBack(routerName) {
      this.$router.push({
        name: routerName,
        params: {
          teacherCourseId: this.$route.params.teacherCourseId
        }
      })
    },
    toAddLessonSchedule() {
      this.$router.push({
        name: 'LessonScheduleAdd',
        query: {
          courseId: this.$route.params.teacherCourseId,
          lessonId: this.$route.params.teacherLessonId
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
  overflow: hidden;
  height: 72px;
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
