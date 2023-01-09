<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="原始密码" prop="oldPwd">
        <el-input v-model="form.oldPwd" type="password" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPwd">
        <el-input v-model="form.newPwd" type="password" />
      </el-form-item>
      <el-form-item label="确认新密码" prop="newPwdAgain">
        <el-input v-model="form.newPwdAgain" type="password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import userService from '@/service/user'

export default {
  name: 'ChangePassword',
  data() {
    const validatePassword = (rule, value, callback) => {
      console.log(this)
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.form.newPwd) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      form: {
        oldPwd: undefined,
        newPwd: undefined,
        newPwdAgain: undefined
      },
      rules: {
        oldPwd: [{ required: true, message: '请输入据原始密码' }],
        newPwd: [{ required: true, message: '请输入据新密码' }],
        newPwdAgain: [{ required: true, message: '请再次输入新密码' }, { validator: validatePassword }]
      }
    }
  },
  methods: {
    confirm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const { code } = await userService.modifyPwd(this.form)
            if (code === 0) {
              this.$message.success('操作成功')
              this.$emit('on-success')
            }
          } catch (e) {
            this.$message.error(e.message)
          }
        }
      })
    },
    cancel() {
      this.$emit('on-cancel')
    }
  }
}
</script>

<style scoped>

</style>
