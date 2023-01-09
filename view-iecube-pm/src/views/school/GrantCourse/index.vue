<template>
  <div>
    <el-table
      ref="courseGrantTable"
      v-loading="loading"
      :data="courseList"
      element-loading-text="加载中"
      fit
      @select="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" />
      <el-table-column label="课程名称" prop="name" align="center" show-overflow-tooltip />
      <el-table-column label="简介" prop="summary" width="200" align="center" show-overflow-tooltip />
      <el-table-column label="所属机构" prop="organizationName" align="center" show-overflow-tooltip />
    </el-table>
    <div style="text-align: center; margin-top: 20px">
      <el-button type="primary" @click="confirm">确定</el-button>
      <el-button @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import courseService from '@/service/course'
import schoolService from '@/service/school'

export default {
  name: 'SchoolGrantCourse',
  props: {
    schoolId: {
      type: Number,
      default: undefined
    }
  },
  data() {
    return {
      loading: false,
      courseList: [],

      selectedCourseList: [],
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listCourse()
    },
    async listCourse() {
      this.loading = true
      try {
        const { model } = await courseService.listAll()
        this.courseList = model.content
        const { model: granted } = await schoolService.listGrantCourse(this.schoolId)
        const map = {}
        for (let i = 0; i < this.courseList.length; i++) {
          map[this.courseList[i].id] = this.courseList[i]
        }

        if (granted && granted.length > 0) {
          for (let i = 0; i < granted.length; i++) {
            this.$refs.courseGrantTable.toggleRowSelection(map[granted[i].id], true)
          }
        }
      } catch (e) {
        console.error(e)
        this.$message.error('系统异常')
      } finally {
        this.loading = false
      }
    },
    handleSelectionChange(selection, row) {
      console.log(selection)
      this.selectedCourseList = selection
    },
    async confirm() {
      console.log(this.selectedCourseList)
      const { code } = await courseService.grant(this.selectedCourseList.map(o => o.id), this.schoolId)
      if (code === 0) {
        this.$emit('on-success')
        this.$message.success('操作成功')
      } else {
        this.$message.error('系统异常')
      }
    },
    cancel() {
      this.$emit('on-cancel')
    }
  }
}
</script>

<style scoped>

</style>
