<template>
  <div class="login-container">
    <el-form ref="ruleForm" :rules="rules" :model="ruleForm" class="login-form">
      <div class="login-tittle">IECube 学生平台</div>
      <div class="login-subtitle">邮箱登录</div>
      <el-form-item prop="email">
        <el-input v-model="ruleForm.email" placeholder="请输入邮箱" class="input email" prefix-icon="el-icon-user" />
      </el-form-item>
      <el-form-item style="margin-bottom: 0" prop="password">
        <el-input v-model="ruleForm.password" 
          placeholder="请输入密码" 
          show-password class="input password" 
          prefix-icon="el-icon-lock" 
          @keyup.enter.native="onSubmit('ruleForm')"
        />
      </el-form-item>
      <div style="text-align: left; font-size: 12px; padding: 12px; color: #2DA2A6;"><span style="cursor: pointer" @click="toResetPassword">忘记密码？</span></div>
      <el-form-item>
        <el-button :loading="loading" type="primary" class="login-button" @click.native.prevent="onSubmit('ruleForm')">登录</el-button>
      </el-form-item>
    </el-form>

    <div v-if="showResetPassword">
      <el-dialog title="重置密码" :visible.sync="showResetPassword" append-to-body>
        <el-form ref="resetPasswordForm" :rules="resetPasswordFormRules" :model="resetPasswordForm" class="login-form">
          <el-form-item prop="email">
            <el-input v-model="resetPasswordForm.email" placeholder="请输入邮箱" class="input email" prefix-icon="el-icon-user" />
          </el-form-item>
          <el-form-item>
            <el-button :loading="resetPasswordLoading" type="primary" class="login-button" @click.native.prevent="resetPassword">重置密码</el-button>
          </el-form-item>
        </el-form>
        <div style="padding: 12px; color: #2DA2A6;">重置密码后，会将新的密码发送至账号邮箱，请注意查收。</div>
      </el-dialog>
    </div>

  </div>
</template>

<script>
import userService from '@/service/user'

export default {
  name: 'Login',
  data () {
    return {
      loading: false,
      ruleForm: {
      },
      rules: {
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '请输入正确的密码', trigger: 'blur' }
        ]
      },
      showResetPassword: false,
      resetPasswordLoading: false,
      resetPasswordForm: {
        email: undefined
      },
      resetPasswordFormRules: {
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    onSubmit (formName) {
      this.$refs[formName].validate(async (valid) => {
        try {
          if (valid) {
            this.loading = true
            await this.$store.dispatch('user/login', this.ruleForm)
            await this.$router.push({ path: '/public_course' })
          }
        } catch (e) {
          console.error(e.message)
          this.$message.error(e.message)
        } finally {
          this.loading = false
        }
      })
    },
    toResetPassword () {
      this.showResetPassword = true
    },
    resetPassword () {
      this.$refs.resetPasswordForm.validate(async (valid) => {
        if (valid) {
          this.resetPasswordLoading = true
          try {
            const { code, message } = await userService.resetPassword(this.resetPasswordForm.email)
            if (code === 0) {
              this.$refs.resetPasswordForm.resetFields()
              this.$message.success('密码重置成功，已发送到账号邮箱，请注意查收')
            } else {
              this.$message.error(message)
            }
          } catch (e) {
            this.$message.error((e && e.message) || '系统异常')
          } finally {
            this.resetPasswordLoading = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
$main: #2DA2A6;

.login-container {
  position: absolute;
  height: 100%;
  width: 100%;
  background-image: url("~@/assets/images/bg.png");
  background-size: cover;
  background-position: center center;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: -1;

  .login-form{
    width: 320px;
    height: 540px;
    background: rgba(255, 255, 255, 0.8);
    box-shadow: 0px 0px 20px 0px rgba(255, 255, 255, 0.5);
    border-radius: 20px;
    text-align: center;
    padding-left: 60px;
    padding-right: 60px;

    .login-tittle{
      font-size: 32px;
      font-weight: 600;
      color:$main;
      line-height: 48px;
      margin-top: 60px;
    }

    .login-subtitle{
      font-size: 20px;
      color: rgba(0, 0, 0, 0.65);
      line-height: 24px;
      margin-top: 20px;
    }

    .input{
      width: 320px;
      height: 40px;
      display: block;
      margin-left: auto;
      margin-right: auto;
    }

    ::v-deep .el-form-item__error{
      margin-left: 30px;
      margin-top: 10px;
      font-size: 14px;
    }

    ::v-deep .el-input__inner{
      border-radius: 20px;
    }

    .email{
      margin-top: 80px;
    }

    .password{
      margin-top: 20px;
    }

    .login-button{
      width: 320px;
      height: 40px;
      background: linear-gradient(315deg, #2DA2A6 0%, #B7EB8F 100%);
      border-radius: 20px;
      border: 0;
      margin-top: 80px;
    }
  }
}
</style>
