<template>
  <div>
    <div style="margin-bottom: 16px">
      <el-form>
        <el-row :gutter="16">
          <el-col :span="4">
            <el-form-item label="课程老师">
              <el-select v-model="query.teacherId" style="width: 100%" placeholder="课程老师" clearable>
                <el-option
                  v-for="item in teacherList"
                  :key="'teacher' + item.id"
                  :value="item.id"
                  :label="item.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="课程">
              <el-select v-model="query.courseId" style="width: 100%" placeholder="课程" clearable @change="listLesson">
                <el-option
                  v-for="item in courseList"
                  :key="'course' + item.id"
                  :value="item.id"
                  :label="item.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="课节">
              <el-select v-model="query.lessonId" style="width: 100%" :placeholder="query.courseId ? '课节' : '请先选择课程'" clearable>
                <el-option
                  v-for="item in lessonList"
                  :key="'lesson' + item.id"
                  :value="item.id"
                  :label="item.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item style="height: 90px">
              <el-button style="margin-top: 40px" type="primary" @click="list">查询</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table
      v-loading="loading"
      :data="data"
      element-loading-text="加载中"
      fit
    >
      <el-table-column label="课程老师" prop="teacherName" />
      <el-table-column label="课程" prop="courseName" />
      <el-table-column label="课节" prop="lessonName" />
      <el-table-column label="开课日期" prop="startDate" />
      <el-table-column label="上课人数" prop="studentCount" />
      <el-table-column label="状态" prop="">
        <template slot-scope="scope">
          <span :class="lessonScheduleStatusEnum.map[status(scope.row)].colorClass">
            {{ status(scope.row) | statusFilter }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="toDetail(scope.row.id)">查看</el-button>
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
import lessonScheduleStatusEnum from '@/enums/lesson-schedule-status-enum'
import userService from '@/service/user'
import dataService from '@/service/data'
export default {
  name: 'LessonSchedule',
  filters: {
    statusFilter: lessonScheduleStatusEnum.filter
  },
  data() {
    return {
      lessonScheduleStatusEnum,
      teacherList: [],
      courseList: [],
      lessonList: [],
      data: [],
      statusMap: {
        'IN_PROGRESS': {
          label: ''
        }
      },
      query: {
        teacherId: undefined,
        courseId: undefined,
        lessonId: undefined
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
      this.listTeacher()
      this.listCourse()
      this.list()
    },
    async listTeacher() {
      const { model } = await userService.listBySchool({ type: 'TEACHER' })
      this.teacherList = model.content
    },
    async listCourse() {
      const { model } = await dataService.listSchoolCourse()
      this.courseList = model
    },
    async listLesson() {
      if (this.query.courseId) {
        const { model } = await dataService.listLessonSchedule(this.query.courseId)
        this.lessonList = model
      } else {
        this.lessonList = []
        this.query.lessonId = undefined
      }
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
      const { model } = await lessonScheduleService.list(params)
      this.data = model.content
      this.pages.totalCount = model.totalCount
    },
    toDetail(id) {
      this.$router.push({
        name: 'LessonScheduleDetail',
        params: {
          lessonScheduleId: id
        }
      })
    },
    status(row) {
      const start = new Date(row.startDate + ' ' + row.startTime)
      const end = new Date(row.endDate + ' ' + row.endTime)
      const now = new Date()
      if (now.getTime() < start.getTime()) {
        return 'NOT_START'
      }
      if (now.getTime() >= start.getTime() && now.getTime() <= end) {
        return 'CURRENT'
      }
      return 'END'
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
