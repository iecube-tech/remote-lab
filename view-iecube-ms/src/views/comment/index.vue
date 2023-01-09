<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>留言管理</span>
        </div>
      </div>
      <div style="margin-bottom: 16px">
        <el-form>
          <el-row :gutter="16">
            <el-col :span="4">
              <el-form-item label="关键字">
                <el-input v-model="query.keywords" placeholder="姓名/邮箱/学号" clearable/>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="学号">
                <el-input v-model="query.num" placeholder="学号（精确查询）" clearable/>
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
                    clearable
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="课程">
                <el-select v-model="query.courseId" style="width: 100%" placeholder="课程" @change="listLesson" clearable>
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
        <el-table-column label="账号" prop="email" min-width="120" show-overflow-tooltip />
        <el-table-column label="姓名" prop="creatorName" min-width="80" show-overflow-tooltip />
        <el-table-column label="课程" prop="courseName" min-width="160" show-overflow-tooltip />
        <el-table-column label="课节" prop="lessonName" min-width="180" show-overflow-tooltip />
        <el-table-column label="详情" prop="content" min-width="200" show-overflow-tooltip />
        <el-table-column label="回复" prop="content" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.reply && scope.row.reply.content }}
          </template>
        </el-table-column>
        <el-table-column label="是否置顶" prop="top" min-width="80" align="center">
          <template slot-scope="scope">
            {{ scope.row.top | topStatusFilter }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" header-align="center" fixed="right">
          <template slot-scope="scope">
            <div style="text-align: center">
              <el-button
                size="mini"
                type="text"
                :class="scope.row.top && 'button-text-warning'"
                @click="top(scope.row.id)"
              >{{ scope.row.top ? '取消置顶' : '置顶' }}
              </el-button>
              <el-divider direction="vertical" />
              <el-button size="mini" type="text" @click="toDetail(scope.row.id)">回复</el-button>
              <el-divider direction="vertical" />
              <el-button
                size="mini"
                type="text"
                class="button-text-danger"
                @click="deleteById(scope.row.id)"
              ><span>删除</span></el-button>
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
    </el-card>

    <div v-if="showDetailDialog">
      <el-dialog title="新增用户" :visible.sync="showDetailDialog">
        <comment-detail :id="currentId" @on-success="showDetailDialog = false" />
      </el-dialog>
    </div>

  </div>
</template>

<script>

import topStatusEnum from '@/enums/comment-top-status-enum'

import dataService from '@/service/data'
import commentService from '@/service/comment'
import CommentDetail from '@/views/comment/Detail'

export default {
  name: 'CommentList',
  components: { CommentDetail },
  filters: {
    topStatusFilter: topStatusEnum.filter
  },
  data() {
    return {
      topStatusEnum,
      data: [],
      facultyList: [],
      gradeList: [],
      gradeClassList: [],
      courseList: [],
      lessonList: [],
      query: {
        keywords: undefined,
        num: undefined,
        faculty: undefined,
        grade: undefined,
        gradeClass: undefined,
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
      showDetailDialog: false,
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
      this.listFaculty()
      this.listGrade()
      this.listGradeClass()
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
    async listCourse() {
      const { model } = await dataService.listSchoolCourse()
      this.courseList = model
    },
    async listLesson() {
      const { model } = await dataService.listLesson(this.query.courseId)
      this.lessonList = model
    },
    async list() {
      const params = Object.assign({}, this.query)
      params.currentPage = this.pages.currentPage
      params.pageSize = this.pages.pageSize
      const { model } = await commentService.listByCurrentTeacher(params)
      this.data = model.content
      this.pages.totalCount = model.totalCount
    },
    toDetail(id) {
      this.currentId = id
      this.showDetailDialog = true
    },
    top(id) {
      this.$confirm(
        '确认置顶该留言吗',
        '确认提示'
      ).then(async() => {
        const { code } = await commentService.top(id)
        if (code === 0) {
          await this.list()
          this.$message.success('置顶成功')
        }
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },
    deleteById(id) {
      this.$confirm(
        '此操作将永久删除该留言, 是否继续？',
        '操作提示'
      ).then(async() => {
        const { code } = await commentService.deleteById(id)
        if (code === 0) {
          this.$message.success('操作成功')
        }
      }).catch(() => {
        this.$message.info('操作取消')
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
</style>
