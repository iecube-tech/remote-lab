<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>设备管理</span>
          <el-button class="list-button" type="text" @click="toAddOrModify('ADD')">
            新增
          </el-button>
        </div>
      </div>
      <div
        style="color: #97a8be; font-size: 12px; padding: 12px; margin-bottom: 8px"
      >当前信令服务地址：{{ signalServerUrl }}</div>
      <el-table
        v-loading="loading"
        :data="data"
        element-loading-text="加载中"
        fit
      >
        <el-table-column label="设备ID" prop="deviceId" />
        <el-table-column label="设备名称" prop="name" />
        <el-table-column label="设备类型" prop="type" />
        <el-table-column label="设备位置" prop="location" show-overflow-tooltip />
        <el-table-column label="直播地址" prop="liveUrl" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态">
          <template slot-scope="scope">
            {{ scope.row.status | enableStatusFilter }}
          </template>
        </el-table-column>
        <el-table-column label="操作" header-align="center">
          <template slot-scope="scope">
            <div style="text-align: center">
              <el-button size="mini" type="text" @click="toAddOrModify('MODIFY', scope.row.id)">编辑</el-button>
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
      <el-dialog :title="type === 'ADD' ? '新增设备' : '编辑设备'" :visible.sync="showAddOrModifyDialog">
        <device-add-or-modify
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

import enableStatusEnum from '@/enums/enable-status-enum'
import DeviceAddOrModify from '@/views/device/AddOrModify'
import deviceService from '@/service/device'

export default {
  name: 'DeviceList',
  components: { DeviceAddOrModify },
  filters: {
    enableStatusFilter: enableStatusEnum.filter
  },
  data() {
    return {
      enableStatusEnum,
      type: 'ADD',
      data: [],
      query: {},
      pages: {
        currentPage: 1,
        pageSizes: [10, 20, 30, 40],
        pageSize: 10,
        totalCount: 0,
        layout: 'total, sizes, prev, pager, next'
      },
      signalServerUrl: undefined,
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
      this.getSignalServerUrl()
    },
    async getSignalServerUrl() {
      const { model } = await deviceService.getSignalServerUrl()
      this.signalServerUrl = model
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
      const { model } = await deviceService.list(params)
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
        '此操作将永久删除该设备, 是否继续？',
        '操作提示'
      ).then(async() => {
        const { code } = await deviceService.deleteById(id)
        if (code === 0) {
          await this.list()
          this.$message.success('操作成功')
        }
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },
    onSuccess() {
      this.list()
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

.list-button {
  float: right;
  padding: 3px 0;
  margin-left: 16px
}
</style>
