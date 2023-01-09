<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <div slot="header">
          <span>学校管理</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="toAddOrModify('ADD')">新增</el-button>
        </div>
      </div>
      <el-table
        v-loading="loading"
        :data="data"
        element-loading-text="加载中"
        fit
      >
        <el-table-column label="名称" prop="name"/>
        <el-table-column label="管理员" prop="adminName"/>
        <el-table-column label="管理员邮箱" prop="adminEmail"/>
        <el-table-column label="状态" prop="status">
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
              <el-divider direction="vertical"/>
              <el-button
                size="mini"
                type="text"
                class="button-text-danger"
                @click="toGrantCourse(scope.row.id)"
              >授权课程
              </el-button>
              <el-divider direction="vertical"/>
              <el-button
                size="mini"
                type="text"
                class="button-text-danger"
                @click="toRoleGrant(scope.row.id)"
              >角色授权
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div v-if="showAddOrModifyDialog">
      <el-dialog title="编辑" :visible.sync="showAddOrModifyDialog" width="1024px">
        <school-add-or-modify :id="currentId" :type="type" @on-success="onSuccess" @on-cancel="showAddOrModifyDialog = false"/>
      </el-dialog>
    </div>

    <div v-if="showGrantCourseDialog">
      <el-dialog title="授权课程" :visible.sync="showGrantCourseDialog" width="1024px">
        <school-grant-course :school-id="currentId" @on-success="onSuccess" @on-cancel="showGrantCourseDialog = false"/>
      </el-dialog>
    </div>

    <div v-if="showRoleGrantDialog">
      <el-dialog title="角色授权" :visible.sync="showRoleGrantDialog" width="1024px">
        <school-role-grant :school-id="currentId" @on-success="onSuccess" @on-cancel="showRoleGrantDialog = false" />
      </el-dialog>
    </div>

  </div>
</template>

<script>

import SchoolAddOrModify from '@/views/school/AddOrModify'
import schoolService from '@/service/school'
import SchoolGrantCourse from '@/views/school/GrantCourse'
import SchoolRoleGrant from '@/views/school/RoleGrant'
import enableStatusEnum from '@/enums/enable-status-enum'

export default {
  components: { SchoolRoleGrant, SchoolGrantCourse, SchoolAddOrModify },
  filters: {
    enableStatusFilter: enableStatusEnum.filter
  },
  data() {
    return {
      data: [],
      type: 'ADD',
      showAddOrModifyDialog: false,
      showGrantCourseDialog: false,
      showRoleGrantDialog: false,
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
    async list() {
      this.loading = true
      try {
        const { model } = await schoolService.list({})
        this.data = model.content
      } catch (e) {
        this.$message.error('获取学校列表失败!')
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
      this.list()
      this.showAddOrModifyDialog = false
      this.showGrantCourseDialog = false
      this.showRoleGrantDialog = false
    },
    toGrantCourse(id) {
      this.currentId = id
      this.showGrantCourseDialog = true
    },
    toRoleGrant(id) {
      this.currentId = id
      this.showRoleGrantDialog = true
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
