<template>
  <div>
    <div>
      <el-form ref="form" :model="query" label-position="top">
        <el-row :gutter="16">
          <el-col :span="4" class="query-col">
            <el-form-item label="课程" :rules="[ { required: true, message: '请先选择课程' } ]" prop="courseId">
              <el-select v-model="query.courseId" style="width: 100%" placeholder="课程" clearable>
                <el-option
                  v-for="item in courseList"
                  :key="'homeworkQueryCourse' + item.id"
                  :value="item.id"
                  :label="item.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4" class="query-col">
            <el-form-item label="学号">
              <el-input v-model="query.num" placeholder="学号" clearable/>
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
          <el-col :span="4" class="query-col">
            <el-form-item label="" style="height: 90px; margin-top: 50px">
              <el-button type="primary" @click="list">查询</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table
      v-loading="loading"
      :data="data"
      element-loading-text="加载中"
      :empty-text="query.courseId ? '暂无数据' : '请先选择课程'"
      fit
    >
      <el-table-column label="学号/工号" prop="num" width="100" fit/>
      <el-table-column label="姓名" prop="studentName" width="100"/>
      <el-table-column label="学院" prop="faculty" width="100"/>
      <el-table-column label="年级" prop="grade" width="100"/>
      <el-table-column label="班级" prop="gradeClass" width="100"/>
      <el-table-column label="总分" prop="totalPoints" width="80"/>
      <el-table-column label="加权后总分" prop="weightTotalPoints" width="100" align="center"/>

      <el-table-column
        v-for="item in lessonList"
        :key="'lessonColumn' + item.id"
        :label="item.name"
        min-width="180"
        align="center"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.lessonHomeworkMap[item.id]">
            {{ scope.row.lessonHomeworkMap[item.id].score }}
            <el-divider direction="vertical"/>
            {{ scope.row.lessonHomeworkMap[item.id].weight + '%' }}
          </div>
          <div v-else>暂无作业信息</div>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="120" header-align="center" fixed="right">
        <template slot-scope="scope">
          <div style="text-align: center">
            <el-button
              :loading="scope.row.loading"
              size="mini"
              type="text"
              @click="packingDownloadByStudentId(scope.row.studentId, scope.$index)"
            >下载作业
            </el-button>
          </div>
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
        @size-change="list"
        @current-change="list"
      />
    </div>
  </div>
</template>

<script>
import homeworkService from '@/service/homework'
import dataService from '@/service/data'
import teacherCourseService from '@/service/teacher-course'
import lessonService from '@/service/lesson'

export default {
  name: 'HomeworkCourseDetail',
  data() {
    return {
      courseList: [],
      facultyList: [],
      gradeList: [],
      gradeClassList: [],
      lessonList: [],
      data: [],
      query: {
        courseId: undefined,
        num: undefined,
        faculty: undefined,
        grade: undefined,
        gradeClass: undefined
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
    if (this.$route.query.courseId) {
      this.query.courseId = parseInt(this.$route.query.courseId.toString())
    }
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listCourse()
      this.listFaculty()
      this.listGrade()
      this.listGradeClass()
    },
    async listCourse() {
      const { model } = await teacherCourseService.listGrant({ pageMode: 0 })
      this.courseList = model.content
      if (!this.query.courseId && this.courseList && this.courseList.length > 0) {
        this.query.courseId = this.courseList[0].id
      }
      await this.listLesson()
      await this.list()
    },
    async listLesson() {
      const { model } = await lessonService.listByCourseId(this.query.courseId)
      this.lessonList = model
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
    async list() {
      this.$refs['form'].validate(async valid => {
        if (valid) {
          const params = Object.assign({}, this.query)
          params.currentPage = this.pages.currentPage
          params.pageSize = this.pages.pageSize
          const { model } = await homeworkService.courseDetail(params)
          this.data = model.content
          this.pages.totalCount = model.totalCount
          for (let i = 0; i < this.data.length; i++) {
            const lessonHomeworkMap = {}
            for (let j = 0; j < this.data[i].lessonHomeworkList.length; j++) {
              lessonHomeworkMap[this.data[i].lessonHomeworkList[j].lessonId] = this.data[i].lessonHomeworkList[j]
            }
            this.data[i]['lessonHomeworkMap'] = lessonHomeworkMap
          }
        }
      })
    },
    async packingDownloadByStudentId(studentId, index) {
      this.$set(this.data[index], 'loading', true)
      try {
        const { code } = await homeworkService.packagingDownload(studentId, this.query.courseId)
        if (code === 0) {
          this.$message.success('操作成功')
        }
      } catch (e) {
        console.error(e)
        this.$message.error(e.message)
      } finally {
        this.$set(this.data[index], 'loading', false)
      }
    }
  }
}
</script>
<style scoped>
.page-wrapper {
  margin-top: 12px;
  text-align: center;
}

.query-col .el-form-item {
  margin-bottom: 0;
}
</style>
