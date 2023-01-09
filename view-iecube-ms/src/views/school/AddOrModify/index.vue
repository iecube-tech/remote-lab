<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" label-width="100px">
      <el-form-item label="名称">
        <el-input v-model="form.name"/>
      </el-form-item>
      <el-form-item label="管理员名称">
        <el-input v-model="form.adminName"/>
      </el-form-item>
      <el-form-item label="管理员邮箱">
        <el-input v-model="form.adminEmail"/>
      </el-form-item>
      <el-form-item label="二级域名">
        <el-input v-model="form.sld"/>
      </el-form-item>
      <el-form-item label="首页背景">
        <el-input v-model="form.homePageBackgroundUrl	"/>
      </el-form-item>
      <el-form-item label="Logo">
        <el-input-number v-model="form.logoUrl"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择">
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirm">创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'SchoolAddOrModify',
  props: {
    id: {
      type: Number,
      default: undefined
    },
    type: {
      type: String,
      default: 'ADD'
    }
  },
  data() {
    return {

      form: {
        name: undefined,
        adminName: undefined,
        adminEmail: undefined,
        sld: undefined,
        homePageBackgroundUrl: undefined,
        logoUrl: 1,
        status: undefined
      }
    }
  },
  mounted() {

  },
  methods: {
    fetchData() {
      if (this.type === 'MODIFY' && this.id) {
        this.get()
      }
    },
    get() {

    },
    confirm() {
      const params = Object.assign({}, this.form)
      if (this.type === 'ADD') {

      } else if (this.type === 'MODIFY') {
        params.id = this.id

      }
    }
  }
}
</script>

<style scoped>

</style>
