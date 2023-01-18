<template>
  <div>
    <el-row :gutter="20">
      <el-col v-for="item in data" :key="'school-course' + item.id" :span="8">
        <div style="cursor: pointer;" @click="toDetail(item.id)">
          <el-card style="width: 100%; height: 160px; margin-bottom: 20px;" @click="toDetail(item.id)">
            <el-row>
              <el-col :span="9">
                <img :src="'local-resource/' + item.coverUrl" height="120px" width="120px" :alt="item.name">
              </el-col>
              <el-col :span="15">
                <div class="course-name">{{ item.name }}</div>
                <div class="course-summary">{{ item.summary }}</div>
                <div class="course-provider">Provider：{{ item.provider }}</div>
              </el-col>
            </el-row>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import schoolCourseService from '@/service/school-course'

export default {
  name: 'SchoolCourseList',
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
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.list()
    },
    async list() {
      const { model } = await schoolCourseService.list({ pageMode: 0 })
      this.data = model.content
    },
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
  height: 48px;
}

.course-provider {
  font-size: 16px;
  color: #606266;
  letter-spacing: 0;
  line-height: 16px;
  font-weight: 400;
}
</style>
