<template>
  <div>
    <el-card v-for="item in roleList" :key="'role' + item.code" style="margin-bottom: 20px">
      <div slot="header">{{ item.name }}</div>
      <div>
        <el-checkbox-group v-model="item.permissionCodeList">
          <el-row :gutter="8">
            <el-col
              v-for="permission in permissionList"
              :key="'item' + permission.code"
              :span="4"
              style="margin-bottom: 4px"
            >
              <el-checkbox :label="permission.code">{{ permission.name }}</el-checkbox>
            </el-col>
          </el-row>
        </el-checkbox-group>
      </div>
    </el-card>
    <div style="text-align: center; margin-top: 20px">
      <el-button type="primary" @click="confirm">确定</el-button>
      <el-button @click="$emit('on-cancel')">取消</el-button>
    </div>
  </div>
</template>

<script>
import roleService from '@/service/role'
import permissionService from '@/service/permission'

export default {
  name: 'SchoolRoleGrant',
  props: {
    schoolId: {
      type: Number,
      default: undefined
    }
  },
  data() {
    return {
      roleList: [],
      permissionList: []
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listRole()
      this.listPermission()
    },
    async listRole() {
      const { model } = await roleService.listRoleBySchoolId(this.schoolId)
      this.roleList = model
    },
    async listPermission() {
      const { model } = await permissionService.listAll()
      this.permissionList = model
    },
    confirm() {
      this.submit()
    },
    async submit() {
      await roleService.grant({ schoolId: this.schoolId, roleList: this.roleList })
      this.$message.success('操作成功')
      this.$emit('on-success')
    }
  }
}

</script>

<style scoped>

</style>
