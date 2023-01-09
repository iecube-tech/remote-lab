<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>机构管理</span>
          <el-button style="float: right; padding: 3px 0" size="text" @click="toAddOrModify('ADD')">新增</el-button>
        </div>
      </div>
      <el-table
        v-loading="loading"
        :data="data"
        element-loading-text="加载中"
        fit
      >
        <el-table-column label="机构名称" prop="name" />
        <el-table-column label="机构管理员名称" prop="adminName" />
        <el-table-column label="机构管理员邮箱" prop="adminEmail" />
        <el-table-column label="状态">
          <template slot-scope="scope">
            <span>{{ scope.row.status | enableStatusFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="toAddOrModify('MODIFY',scope.row.id)">编辑</el-button>
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
      <el-dialog :visible.sync="showAddOrModifyDialog">
        <div slot="title">{{ dialogTitle }}</div>
        <organization-add-or-modify :id="currentId" :type="type" @on-success="onSuccess" @on-cancel="showAddOrModifyDialog=false" />
      </el-dialog>

    </div>

  </div>
</template>

<script>
import OrganizationAddOrModify from '@/views/organization/AddOrModify'
import organizationService from '@/service/organization'
import enableStatusEnum from '@/enums/enable-status-enum'

export default {
  components: { OrganizationAddOrModify },
  filters: {
    enableStatusFilter: enableStatusEnum.filter
  },
  data() {
    return {
      enableStatusEnum,
      data: [],
      showAddOrModifyDialog: false,
      currentId: undefined,
      loading: false,
      dialogTitle: undefined,
      type: undefined,
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
    // 请求机构列表数据
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
        const { model } = await organizationService.list(params)
        this.data = model.content
        this.pages.totalCount = model.totalCount
      } catch (e) {
        console.log(e)
        this.$message.error('系统异常')
      }
      this.loading = false
    },
    toAddOrModify(type, id) {
      this.type = type
      this.dialogTitle = '新增机构'
      if (this.type === 'MODIFY') {
        this.currentId = id
        this.dialogTitle = '编辑机构'
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
