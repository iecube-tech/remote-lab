<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" label-width="100px" :rules="rules">
      <el-form-item label="机构名称" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="管理员名称" prop="adminName">
        <el-input v-model="form.adminName" />
      </el-form-item>
      <el-form-item label="管理员邮箱" prop="adminEmail">
        <el-input v-model="form.adminEmail" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择">
          <el-option
            v-for="item in enableStatusEnum.enum"
            :key="'status' + item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirm">创建</el-button>
        <el-button @click="closeDialog">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import organizationService from '@/service/organization'
import enableStatusEnum from '@/enums/enable-status-enum'

export default {
  name: 'OrganizationAddOrModify',
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
      enableStatusEnum,
      options: [{
        value: 'ENABLE',
        label: '激活'
      }, {
        value: 'DISABLE',
        label: '未激活'
      }],
      form: {
        name: undefined,
        adminName: undefined,
        adminEmail: undefined,
        status: undefined
      },
      rules: {
        name: [{ required: true, message: '机构名称不能为空', trigger: 'blur' }],
        adminName: [{ required: true, message: '机构管理员名称不能为空', trigger: 'blur' }],
        adminEmail: [{ required: true, message: '机构邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
        status: [{ required: true, message: '请选择激活状态', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    // 请求单个机构数据渲染到页面
    fetchData() {
      if (this.type === 'MODIFY' && this.id) {
        this.get()
      }
    },
    async get() {
      const { model } = await organizationService.get(this.id)
      this.form = model
    },
    confirm() {
      // 表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          const params = Object.assign({}, this.form)
          if (this.type === 'ADD') {
            console.log(this.type + ':' + params)
            organizationService.add(params).then(res => {
              this.$emit('on-success')
            }).catch(e => {
              console.error(e)
              this.$message.error('系统异常')
            })
          } else if (this.type === 'MODIFY') {
            params.id = this.id
            console.log(params.id + ':' + this.type + ':' + params)
            organizationService.edit(params).then(res => {
              this.$emit('on-success')
            }).catch(e => {
              console.error(e)
              this.$message.error('系统异常')
            })
          }
        }
      })
    },
    // 取消点击 关闭弹层
    closeDialog() {
      this.$emit('on-cancel')
    }
  }
}
</script>

<style scoped>

</style>
