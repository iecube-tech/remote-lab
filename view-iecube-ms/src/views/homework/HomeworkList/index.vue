<template>
  <div>
    <div style="margin-bottom: 16px">
      <el-form>
        <el-row :gutter="16">
          <el-col :span="4">
            <el-form-item label="关键字">
              <el-input v-model="query.keywords" placeholder="姓名/邮箱/学号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="学号">
              <el-input v-model="query.num" placeholder="学号（精确查询）" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="4" class="query-col">
            <el-form-item label="学院">
              <el-select v-model="query.faculty" style="width: 100%" placeholder="学院" clearable>
                <el-option
                  v-for="item in facultyList"
                  :key="'faculty' + item"
                  :value="item"
                  :label="item"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4" class="query-col">
            <el-form-item label="年级">
              <el-select v-model="query.grade" style="width: 100%" placeholder="年级" clearable>
                <el-option
                  v-for="item in gradeList"
                  :key="'grade' + item"
                  :value="item"
                  :label="item"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4" class="query-col">
            <el-form-item label="班级">
              <el-select v-model="query.gradeClass" style="width: 100%" placeholder="班级" clearable>
                <el-option
                  v-for="item in gradeClassList"
                  :key="'gradeClass' + item"
                  :value="item"
                  :label="item"
                />
              </el-select>
            </el-form-item>
          </el-col>
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
              <el-select
                v-model="query.lessonId"
                style="width: 100%"
                :placeholder="query.courseId ? '课节' : '请先选择课程'"
                clearable
              >
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
      <el-table-column label="学号/工号" prop="num" />
      <el-table-column label="姓名" prop="studentName" />
      <el-table-column label="学院" prop="faculty" />
      <el-table-column label="年级" prop="grade" />
      <el-table-column label="班级" prop="gradeClass" />
      <el-table-column label="课程" prop="courseName" />
      <el-table-column label="课节" width="200" prop="lessonName" />
      <el-table-column label="作业" width="200">
        <template slot-scope="scope">
          <a
            target="_blank"
            :href="'local-resource/' + (scope.row.resource && scope.row.resource.key)"
            style="color: #409EFF"
          >
            {{ scope.row.resource && scope.row.resource.filename }}
          </a>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" prop="createTime" width="150" />
      <el-table-column label="授课老师" prop="teacherName" />
      <el-table-column label="分数" prop="score" />
      <el-table-column label="操作" width="120" header-align="center" fixed="right">
        <template slot-scope="scope">
          <div style="text-align: center">
            <el-button
              v-if="!scope.row.score"
              size="mini"
              type="text"
              @click="rating(scope.row.studentId, scope.row.score,scope.row.lessonScheduleId)"
            >评分
            </el-button>
            <el-button
              v-if="scope.row.score"
              v-permission="'HOMEWORK_SCORE_MODIFY'"
              size="mini"
              type="text"
              @click="rating(scope.row.studentId, scope.row.score,scope.row.lessonScheduleId)"
            >修改分数
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="showAddOrModifyDialog">
      <el-dialog :title="'评分'" :visible.sync="showAddOrModifyDialog">
        <el-form ref="scoreForm" :inline="true" :rules="scoreFormRules" :model="scoreForm" class="demo-form-inline">
          <el-form-item label="分数" prop="score">
            <el-input-number v-model="scoreForm.score" :min="0" :max="100" :step="0.1" step-strictly placeholder="请输入分数" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitScore">提交</el-button>
            <el-button @click="cancel">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
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
import homeworkService from '@/service/homework'
import dataService from '@/service/data'
import userService from '@/service/user'

export default {
  name: 'HomeworkList',
  data() {
    return {
      data: [],
      facultyList: [],
      gradeList: [],
      gradeClassList: [],
      teacherList: [],
      courseList: [],
      lessonList: [],
      query: {
        keywords: undefined,
        num: undefined,
        faculty: undefined,
        grade: undefined,
        classroom: undefined,
        teacherId: undefined,
        courseId: undefined,
        lessonId: undefined
      },
      pages: {
        currentPage: 1,
        pageSizes: [10, 20, 30, 40],
        pageSize: 10,
        totalCount: 100,
        layout: 'total, sizes, prev, pager, next'
      },
      scoreFormRules: {},
      showAddOrModifyDialog: false,
      currentId: undefined,
      loading: false,
      scoreForm: {
        score: 0.0,
        studentId: 0,
        lessonScheduleId: 0
      },
      hasModifyScorePermission: false
    }
  },
  mounted() {
    this.hasModifyScorePermission = this.$hasPermission('HOMEWORK_SCORE_MODIFY')
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.list()
      this.listFaculty()
      this.listGrade()
      this.listGradeClass()
      this.listTeacher()
      this.listCourse()
    },
    async listFaculty() {
      const { model } = await dataService.listFaculty()
      this.facultyList = model
    },
    async listGrade() {
      const { model } = await dataService.listGrade()
      this.gradeList = model
    },
    async listGradeClass() {
      const { model } = await dataService.listGradeClass()
      this.gradeClassList = model
    },
    async listTeacher() {
      const { model } = await userService.listBySchool({ type: 'TEACHER', pageMode: 0 })
      this.teacherList = model.content
    },
    async listCourse() {
      const { model } = await dataService.listTeacherCourse({})
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
      const { model } = await homeworkService.list(params)
      this.data = model.content
      this.pages.totalCount = model.totalCount
    },
    rating(id, score, lessonScheduleId) {
      this.showAddOrModifyDialog = true
      this.scoreForm.studentId = id
      this.scoreForm.score = score
      this.scoreForm.lessonScheduleId = lessonScheduleId
    },
    cancel() {
      this.showAddOrModifyDialog = false
      this.fetchData()
    },
    submitScore() {
      this.$refs.scoreForm.validate(async valid => {
        if (valid) {
          const { code, message } = await homeworkService.rating(this.scoreForm)
          if (code === 0) {
            await this.list()
            this.showAddOrModifyDialog = false
            this.$message.success('操作成功')
          } else {
            this.$message.error(message)
          }
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
