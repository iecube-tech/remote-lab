<template>
  <div>
    <div>
      <el-table
        v-loading="loading"
        :data="data"
        element-loading-text="加载中"
        fit
      >
        <el-table-column label="课程名称" width="200" prop="courseName" />
        <el-table-column label="共收作业书" width="200" prop="homeworkCount" />
        <el-table-column label="操作" width="120" header-align="center">
          <template slot-scope="scope">
            <div style="text-align: center">
              <el-button size="mini" type="text" @click="toDetail(scope.row.courseId)">查看全部作业</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import homeworkService from '@/service/homework'

export default {
  name: 'HomeworkStatistics',
  data() {
    return {
      data: [],
      loading: false
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.statistics()
    },
    async statistics() {
      this.loading = true
      try {
        const { model } = await homeworkService.statistics({})
        this.data = model.content
      } catch (e) {
        this.$message.error('系统异常')
      }
      this.loading = false
    },
    toDetail(courseId) {
      this.$router.push({
        name: 'HomeworkCourseDetail',
        query: {
          courseId: courseId
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

.list-button {
  float: right;
  padding: 3px 0;
  margin-left: 16px
}
</style>
