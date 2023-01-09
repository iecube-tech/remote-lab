<template>
  <div>
    <el-table
      v-loading="loading"
      :data="data"
      element-loading-text="加载中"
      fit
    >
      <el-table-column label="课程" prop="courseName" />
      <el-table-column label="课节" prop="lessonName" />
      <el-table-column label="开课日期" prop="startDate" />
      <el-table-column label="结束日期" prop="endDate" />
      <el-table-column label="助教" prop="assistantName" />
      <el-table-column label="上课人数" prop="studentCount" align="center" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="toDetail(scope.row.id)">查看</el-button>
          <el-divider v-if="type === 'CURRENT'" direction="vertical" />
          <el-button
            v-if="type === 'CURRENT'"
            type="text"
            size="mini"
            class="button-text-warning"
            @click="toAddOrModify(scope.row.id)"
          >编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-wrapper">
      <el-pagination
        background
        :current-page="pages.currentPage"
        :page-sizes="pages.pageSizes"
        :page-size="pages.pageSize"
        :layout="pages.layout"
        :total="pages.totalCount"
        @size-change="pageSizeChange"
        @current-change="pageChange"
      />
    </div>

  </div>
</template>

<script>
import lessonScheduleService from '@/service/lesson-schedule'

export default {
  name: 'LessonScheduleList',
  props: {
    type: {
      type: String,
      default: 'CURRENT'
    }
  },
  data() {
    return {
      data: [],
      query: {
        keywords: undefined
      },
      pages: {
        currentPage: 1,
        pageSizes: [10, 20, 30, 40],
        pageSize: 10,
        totalCount: 0,
        layout: 'total, sizes, prev, pager, next'
      },
      showAddOrModifyDialog: false,
      currentId: undefined,
      loading: false
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.list()
    },
    pageChange(currentPage) {
      this.pages.currentPage = currentPage
      this.list()
    },
    pageSizeChange(pageSize) {
      this.pages.pageSize = pageSize
      this.list()
    },
    async list() {
      const params = Object.assign({}, this.query)
      params.currentPage = this.pages.currentPage
      params.pageSize = this.pages.pageSize
      params.timeStatus = this.type
      const { model } = await lessonScheduleService.listByCurrentUser(params)
      this.data = model.content
      this.pages.totalCount = model.totalCount
    },
    toAddOrModify(id) {
      this.$router.push({
        name: 'LessonScheduleModify',
        params: { lessonScheduleId: id }
      })
    },
    toDetail(id) {
      this.$router.push({
        name: 'LessonScheduleDetail',
        params: {
          lessonScheduleId: id
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
