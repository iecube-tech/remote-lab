<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>机构用户管理</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="toAddOrModify('ADD')">新增</el-button>
        </div>
      </div>
      <el-table
        v-loading="loading"
        :data="data"
        element-loading-text="加载中"
        fit
      >
        <el-table-column label="工号" prop="num" />
        <el-table-column label="姓名" prop="name" />
        <el-table-column label="邮箱" prop="email" />
        <el-table-column label="状态">
          <template slot-scope="scope">
            {{ scope.row.status | enableStatusFilter }}
          </template>
        </el-table-column>
        <el-table-column label="操作" header-align="center" width="200">
          <template slot-scope="scope">
            <div style="text-align: center">
              <el-button
                size="mini"
                type="text"
                @click="toAddOrModify('MODIFY', scope.row.id)"
              >编辑
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
      <el-dialog title="编辑" :visible.sync="showAddOrModifyDialog">
        <org-user-add-or-modify
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

import userService from '@/service/user'
import OrgUserAddOrModify from '@/views/org-user/AddOrModify'
import enableStatusEnum from '@/enums/enable-status-enum'

export default {
  components: { OrgUserAddOrModify },
  filters: {
    enableStatusFilter: enableStatusEnum.filter
  },
  data() {
    return {
      enableStatusEnum,
      data: [],
      type: 'ADD',
      showAddOrModifyDialog: false,
      currentId: undefined,
      loading: false,
      pages: {
        currentPage: 1,
        pageSizes: [10, 20, 30, 40],
        pageSize: 10,
        totalCount: 0,
        layout: 'total, sizes, prev, pager, next'
      }
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
      this.loading = true
      try {
        const params = {}
        params.currentPage = this.pages.currentPage
        params.pageSize = this.pages.pageSize
        const { model } = await userService.listByOrganization(params)
        this.data = model.content
        this.pages.totalCount = model.totalCount
      } catch (e) {
        this.$message.error('获取用户列表失败!')
        console.log(e)
      }
      this.loading = false
    },
    toAddOrModify(type, id) {
      this.type = type
      if (this.type === 'MODIFY') {
        this.currentId = id
      }
      this.showAddOrModifyDialog = true
    },
    onSuccess() {
      this.fetchData()
      this.showAddOrModifyDialog = false
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
</style>
