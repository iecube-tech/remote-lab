<template>
  <div class="app-container">
    <h3>用户列表</h3>
    <div style="margin-bottom: 20px">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="query.keywords" placeholder="关键字" />
        </el-col>
        <el-col :span="4">
          <el-input v-model="query.num" placeholder="学号/工号" />
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.faculty" style="width: 100%" placeholder="学院">
            <el-option v-for="item in facultyList" :key="'faculty' + item" :value="item" :label="item" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.grade" style="width: 100%" placeholder="年级">
            <el-option v-for="item in gradeList" :key="'grade' + item" :value="item" :label="item" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.gradeClass" style="width: 100%" placeholder="班级">
            <el-option v-for="item in gradeClassList" :key="'gradeClass' + item" :value="item" :label="item" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="list">查询</el-button>
        </el-col>
      </el-row>
    </div>
    <el-table
      ref="userSelectTable"
      v-loading="loading || (!toggled && multiple)"
      :data="data"
      element-loading-text="加载中"
      fit
      :highlight-current-row="!multiple"
      :row-style="{cursor: 'pointer'}"
      @current-change="handleCurrentChange"
      @select="handleSelectionChange"
      @select-all="handleSelectAll"
    >
      <el-table-column v-if="multiple" type="selection" width="55" align="center" />
      <el-table-column label="学号/工号" prop="num" width="120" align="center" />
      <el-table-column label="姓名" prop="name" width="120" align="center" />
      <el-table-column label="类型" prop="name" width="120" align="center" />
      <el-table-column label="学院" prop="faculty" width="120" align="center" />
      <el-table-column label="年级" prop="grade" width="120" align="center" />
      <el-table-column label="班级" prop="gradeClass" width="120" align="center" />
      <el-table-column label="邮箱" prop="email" min-width="240" align="center" />
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
    <h3>已选择</h3>
    <el-table
      v-loading="loading"
      :data="selected"
      fit
    >
      <el-table-column label="学号/工号" prop="num" width="120" align="center" />
      <el-table-column label="姓名" prop="name" width="120" align="center" />
      <el-table-column label="学院" prop="faculty" width="120" align="center" />
      <el-table-column label="年级" prop="grade" width="120" align="center" />
      <el-table-column label="班级" prop="classroom" width="120" align="center" />
      <el-table-column label="邮箱" prop="email" min-width="240" align="center" />
    </el-table>
    <div style="margin-top: 24px; text-align: center">
      <el-button style="width: 160px; margin-right: 24px" type="primary" @click="confirm">确认</el-button>
      <el-button style="width: 160px" @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>

import userService from '@/service/user'
import dataService from '@/service/data'

export default {
  name: 'UserSelectTable',
  props: {
    type: {
      type: [String, Array],
      default: undefined
    },
    selectedIn: {
      type: Array,
      default: undefined
    },
    multiple: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      data: [],
      selected: [],
      facultyList: [],
      gradeList: [],
      gradeClassList: [],
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
      toggled: false,
      loading: false
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      this.init()
      this.list()
      this.listFaculty()
      this.listGrade()
      this.listGradeClass()
      this.loading = false
    },
    async init() {
      if (this.selectedIn && this.selectedIn.length > 0) {
        const { model } = await userService.listBySchool({ idIn: this.selectedIn.map(o => o.id), pageMode: 0 })
        this.selected = model.content
      }
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
      if (this.type) {
        if (typeof this.type === 'string') {
          params.type = this.type
        } else if (this.type instanceof Array) {
          params.typeList = this.type
        }
      }
      params.currentPage = this.pages.currentPage
      params.pageSize = this.pages.pageSize
      const { model } = await userService.listBySchool(params)
      this.data = model.content
      this.pages.totalCount = model.totalCount

      if (this.multiple) {
        const that = this
        const map = {}
        for (let i = 0; i < this.data.length; i++) {
          map[this.data[i].id] = this.data[i]
        }
        if (this.selected && this.selected.length > 0) {
          let flag = false
          console.log(flag)
          let toggleRowSelectionCount = 0
          let needToggleRowSelectionCount = 0
          for (let i = 0; i < this.selected.length; i++) {
            if (map[this.selected[i].id]) {
              needToggleRowSelectionCount++
              flag = true
              console.log(flag)
              map[this.selected[i].id].selected = true
              setTimeout(() => {
                that.$refs.userSelectTable.toggleRowSelection(map[this.selected[i].id], true)
                toggleRowSelectionCount++
                if (needToggleRowSelectionCount === toggleRowSelectionCount) {
                  that.toggled = true
                }
              })
            }
          }
          console.log(flag)
          if (!flag) {
            that.toggled = true
            console.log(that.toggled)
          }
        } else {
          this.toggled = true
        }
      } else {
        if (this.selectedIn && this.selectedIn.length) {
          for (let i = 0; i < this.data.length; i++) {
            if (this.data[i].id === this.selectedIn[0].id) {
              this.$refs.userSelectTable.setCurrentRow(this.data[i])
            }
          }
        }
      }
    },
    handleCurrentChange(row) {
      if (!this.multiple) {
        this.selected = [row]
      }
    },
    handleSelectionChange(selection, row) {
      row.selected = !row.selected
      if (row.selected) {
        this.selected.push(row)
      } else {
        this.selected.splice(this.selected.findIndex(o => o.id === row.id), 1)
      }
    },
    handleSelectAll(selection) {
      const data = this.data
      for (let i = 0; i < data.length; i++) {
        const index = this.selected.findIndex(o => o.id === data[i].id)
        if (index >= 0) {
          this.selected.splice(index, 1)
        }
      }
      if (selection && selection.length) {
        for (let i = 0; i < selection.length; i++) {
          this.selected.push(selection[i])
        }
      }
    },
    confirm() {
      this.$emit('on-confirm', this.selected)
    },
    cancel() {
      this.$emit('on-cancel')
    }
  }
}
</script>
<style scoped>
.page-wrapper {
  margin-top: 24px;
  margin-bottom: 24px;
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
