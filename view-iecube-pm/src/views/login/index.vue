<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">IECube平台管理系统</h3>
      </div>

      <el-form-item prop="email">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="email"
          v-model="loginForm.email"
          placeholder="请输入邮箱"
          name="email"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password" style="margin-bottom: 0">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="请输入密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <div>
        <el-button type="text" style="padding: 12px" @click.native.prevent="toResetPassword">忘记密码？</el-button>
      </div>
      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px; margin-top: 20px" @click.native.prevent="handleLogin">登录</el-button>
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
        <div style="padding: 12px; color: #66b1ff;">重置密码后，会将新的密码发送至账号邮箱，请注意查收。</div>
      </el-dialog>
    </div>

  </div>
</template>

<script>
import userService from '@/service/user'

export default {
  name: 'Login',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码'))
      } else if (value.length < 6) {
        callback(new Error('密码最少6位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        email: undefined,
        password: undefined
      },
      loginRules: {
        email: [{ required: true, trigger: 'blur', type: 'email', message: '请输入邮箱' }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
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
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(e => {
            this.loading = false
            this.$message({
              message: e.message || '系统异常',
              type: 'error'
            })
          })
        }
      })
    },
    toResetPassword() {
      this.showResetPassword = true
    },
    resetPassword() {
      this.$refs.resetPasswordForm.validate(async(valid) => {
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

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
