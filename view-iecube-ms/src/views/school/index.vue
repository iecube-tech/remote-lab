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
        <el-table-column label="名称" prop="name" />
        <el-table-column label="管理员" prop="adminName" />
        <el-table-column label="管理员邮箱" prop="adminEmail" />
        <el-table-column label="二级域名" prop="sld" />
      </el-table>
    </el-card>

    <div v-if="showAddOrModifyDialog">
      <el-dialog title="新增课程" :visible.sync="showAddOrModifyDialog">
        <school-add-or-modify />
      </el-dialog>
    </div>

  </div>
</template>

<script>

import SchoolAddOrModify from '@/views/school/AddOrModify'
export default {
  components: { SchoolAddOrModify },
  data() {
    return {
      data: [
        {
          name: '清华大学',
          adminName: '超管',
          adminEmail: 'admin@qinghua.com',
          sld: 'qinghua.com'
        },
        {
          name: '北京大学',
          adminName: '超管',
          adminEmail: 'admin@beida.com',
          sld: 'beida.com'
        }
      ],
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
    },
    list() {

    },
    toAddOrModify(type, id) {
      this.type = type
      if (this.type === 'MODIFY') {
        this.currentId = id
      }
      this.showAddOrModifyDialog = true
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
