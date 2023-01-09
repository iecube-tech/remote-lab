<template>
  <div>
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>{{ view[viewType].title }}</span>
          <el-upload
            v-if="viewType === 'IN_SCHOOL'"
            style="float: right; display: inline-block;"
            accept=".xls,.xlsx"
            :before-upload="importByExcel"
            action="/"
          >
            <el-button :loading="importLoading" class="list-button" type="text">导入用户</el-button>
          </el-upload>
          <el-button v-if="viewType === 'IN_SCHOOL'" class="list-button" type="text" @click="toAddOrModify('ADD')">
            新增
          </el-button>
          <el-button v-if="viewType === 'IN_SCHOOL'" class="list-button" type="text" @click="downloadTemplate">
            下载导入模板
          </el-button>
        </div>
      </div>
      <div>
        <el-form>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-form-item label="关键字">
                <el-input v-model="query.keywords" placeholder="名称/邮箱" />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="学号/工号">
                <el-input v-model="query.num" placeholder="学号/工号" />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="学院">
                <el-select v-model="query.faculty" style="width: 100%" placeholder="学院">
                  <el-option v-for="item in facultyList" :key="'faculty' + item" :value="item" :label="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="年级">
                <el-select v-model="query.grade" style="width: 100%" placeholder="年级">
                  <el-option v-for="item in gradeList" :key="'grade' + item" :value="item" :label="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="班级">
                <el-select v-model="query.gradeClass" style="width: 100%" placeholder="班级">
                  <el-option v-for="item in gradeClassList" :key="'gradeClass' + item" :value="item" :label="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item style="height: 90px">
                <el-button style="margin-top: 40px" type="primary" @click="page">查询</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <el-button
        style="margin-bottom: 16px"
        :type="viewType === 'LEAVE_SCHOOL' ? 'primary' : 'warning'"
        size="mini"
        @click="batchUpdateSchoolStatus(viewType === 'LEAVE_SCHOOL' ? 'IN_SCHOOL' : 'LEAVE_SCHOOL')"
      >{{ viewType === 'LEAVE_SCHOOL' ? '批量在校' : '批量离校' }}</el-button>
      <el-table
        v-loading="loading"
        :data="data"
        element-loading-text="加载中"
        fit
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" :selectable="(row, index) => row.type !== 'USER_SCHOOL_ADMIN'" />
        <el-table-column label="学号/工号" prop="num" />
        <el-table-column label="姓名" prop="name" />
        <el-table-column label="学院" prop="faculty" />
        <el-table-column label="年级" prop="grade" />
        <el-table-column label="班级" prop="gradeClass" />
        <el-table-column label="邮箱" prop="email" width="200" />
        <el-table-column label="操作" header-align="center" width="200">
          <template slot-scope="scope">
            <div v-if="scope.row.type === 'USER_SCHOOL_ADMIN'" style="text-align: center">-</div>
            <div v-else style="text-align: center">
              <el-button size="mini" type="text" @click="toAddOrModify('MODIFY', scope.row.id)">编辑</el-button>
              <el-divider direction="vertical" />
              <el-button
                size="mini"
                type="text"
                class="button-text-warning"
                @click="updateStatus(scope.row.id, viewType === 'LEAVE_SCHOOL' ? 'IN_SCHOOL' : 'LEAVE_SCHOOL')"
              >{{ viewType === 'LEAVE_SCHOOL' ? '在校' : '离校' }}
              </el-button>
              <el-divider direction="vertical" />
              <el-button size="mini" type="text" class="button-text-danger" @click="deleteById(scope.row.id)">删除
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
          @size-change="pageSizeChange"
          @current-change="pageChange"
        />
      </div>
    </el-card>

    <div v-if="showAddOrModifyDialog">
      <el-dialog title="新增用户" :visible.sync="showAddOrModifyDialog">
        <user-add-or-modify
          :id="currentId"
          :type="type"
          @on-success="onSuccess"
          @on-cancel="showAddOrModifyDialog = false"
        />
      </el-dialog>
    </div>

  </div>
</template>

<script>

import UserAddOrModify from '@/views/user/AddOrModify'
import userService from '@/service/user'
import dataService from '@/service/data'

export default {
  name: 'UserCommonList',
  components: { UserAddOrModify },
  props: {
    viewType: {
      type: String,
      default: 'IN_SCHOOL'
    }
  },
  data() {
    return {
      view: {
        'IN_SCHOOL': {
          title: '在校人员管理'
        },
        'LEAVE_SCHOOL': {
          title: '离校人员管理'
        },
        'ASSISTANT': {
          title: '助教管理'
        }
      },
      data: [],
      facultyList: [],
      gradeList: [],
      gradeClassList: [],
      query: {
        keywords: undefined,
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
      type: undefined,
      loading: false,
      importLoading: false,
      selected: []
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.page()
      dataService.listFaculty().then(res => {
        this.facultyList = res.model
      })
      dataService.listGrade().then(res => {
        this.gradeList = res.model
      })
      dataService.listGradeClass().then(res => {
        this.gradeClassList = res.model
      })
    },
    pageChange(currentPage) {
      this.pages.currentPage = currentPage
      this.page()
    },
    pageSizeChange(pageSize) {
      this.pages.pageSize = pageSize
      this.page()
    },
    async page() {
      const params = Object.assign({}, this.query)
      params.currentPage = this.pages.currentPage
      params.pageSize = this.pages.pageSize
      if (this.viewType === 'IN_SCHOOL') {
        params.schoolStatus = 'IN_SCHOOL'
      } else if (this.viewType === 'LEAVE_SCHOOL') {
        params.schoolStatus = 'LEAVE_SCHOOL'
      }
      const { model } = await userService.listBySchool(params)
      this.data = model.content
      this.pages.totalCount = model.totalCount
    },
    toAddOrModify(type, id) {
      this.type = type
      if (this.type === 'MODIFY') {
        this.currentId = id
      }
      this.showAddOrModifyDialog = true
    },
    deleteById(id) {
      this.$confirm(
        '此操作将永久删除该用户, 是否继续？',
        '操作提示'
      ).then(async() => {
        const { code } = await userService.deleteById(id)
        if (code === 0) {
          this.fetchData()
          this.$message.success('操作成功')
        }
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },
    updateStatus(id, schoolUserStatus) {
      this.$confirm(
        '确认该操作？',
        '操作提示'
      ).then(async() => {
        const { code } = await userService.updateSchoolStatus([id], schoolUserStatus)
        if (code === 0) {
          this.$message.success('操作成功')
          this.fetchData()
        }
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },
    batchUpdateSchoolStatus(schoolUserStatus) {
      if (!this.selected || this.selected.length < 1) {
        this.$message.error('至少勾选一项')
        return
      }
      this.$confirm(
        '确认该操作？',
        '操作提示'
      ).then(async() => {
        const idList = this.selected.map(o => o.id)
        const { code } = await userService.updateSchoolStatus(idList, schoolUserStatus)
        if (code === 0) {
          this.$message.success('操作成功')
          this.fetchData()
        }
      }).catch(e => {
        if (e === 'cancel') {
          this.$message.info('操作取消')
        } else {
          this.$message.error(e.message)
        }
      })
    },
    onSuccess() {
      this.page()
      this.showAddOrModifyDialog = false
    },
    handleSelectionChange(val) {
      this.selected = val
      console.log(this.selected)
    },
    async downloadTemplate() {
      const { code } = await userService.downloadTemplate()
      if (code === 0) {
        this.$message.success('操作成功')
      }
    },
    importByExcel(file) {
      this.importLoading = true
      this.upload(file)
      return false
    },
    async upload(file) {
      try {
        const { code } = await userService.importByExcel(file)
        if (code === 0) {
          this.$message.success('操作成功')
          this.fetchData()
        }
      } catch (e) {
        console.error(e)
        this.$message.error(e.message)
      } finally {
        this.importLoading = false
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
