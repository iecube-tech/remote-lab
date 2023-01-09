<template>
  <div>
    <Navigation/>
    <div class="info-container">
      <div class="personal-info">
        <div class="tittle">{{ personalInfo.name }}</div>
        <div class="line"></div>
        <div class="base-info">
          <div class="value">
            {{ personalInfo.email }}
            <div class="key">邮箱</div>
          </div>
          <div class="value">
            {{ personalInfo.faculty }}
            <div class="key">学院</div>
          </div>
          <div class="value">
            {{ personalInfo.grade }}
            <div class="key">年级</div>
          </div>
          <div class="value">
            {{ personalInfo.gradeClass }}
            <div class="key">班级</div>
          </div>
          <div class="value">
            {{ personalInfo.num }}
            <div class="key">学号</div>
          </div>
        </div>
      </div>
      <div class="changepwd">
        <div class="tittle">修改密码</div>
        <div class="line"></div>
        <el-form ref="pwdForm" :inline="true" :model="formInline" class="form-inline">
          <el-form-item label="原密码">
            <el-input v-model="formInline.oldPwd" placeholder="请输入原密码" class="input" show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="formInline.newPwd" placeholder="请输入新密码" class="input" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="formInline.newPwdAgain" placeholder="请再次输入新密码" class="input" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" plain round :loading="loadingStatus" @click="onSubmit">修改密码</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="logout-container">
        <el-button type="primary" class="logout-button" @click="logout">退出登录</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import Navigation from '@/components/navigation.vue'
import userService from '@/service/user'

export default {
  name: 'PersonalInfo',
  components: {
    Navigation
  },
  data () {
    return {
      personalInfo: {
        id: 3,
        name: '曲非烟',
        email: 'test@gmail.com',
        faculty: '交通物流学院',
        grade: '2023届',
        gradeClass: '五班',
        num: '123456'
      },
      formInline: {
        oldPwd: '',
        newPwd: '',
        newPwdAgain: ''
      },
      loadingStatus: false
    }
  },
  mounted () {
    this.fetchData()
  },
  methods: {
    fetchData () {
      this.currentUserInfo()
    },
    async currentUserInfo () {
      const { model } = await userService.currentUserInfo()
      this.personalInfo = model
    },
    async onSubmit () {
      const params = Object.assign({}, this.formInline)
      params.id = this.personalInfo.id
      if (params.newPwd !== params.newPwdAgain) {
        this.$message.error('两次输入密码不一致')
        return
      }
      try {
        this.loadingStatus = true
        const { code, message } = await userService.modifyPwd(params)
        if (code === 0) {
          this.$message.success('密码修改成功')
          this.formInline = {
            oldPwd: '',
            newPwd: '',
            newPwdAgain: ''
          }
        } else {
          this.$message.error(message)
        }
      } catch (e) {
        this.$message.error(e.message)
      } finally {
        this.loadingStatus = false
      }
    },
    async logout () {
      await this.$store.dispatch('user/logout')
      await this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.info-container {
  margin-top: 80px;
  margin-left: 20px;
  margin-right: 20px;

  .personal-info {
    width: calc(100vw - 40px);
    background: #ffffff;
    box-shadow: 0px 0px 16px 0px rgba(0, 0, 0, 0.1);
  }

  .tittle {
    font-size: 16px;
    font-weight: 600;
    color: #2da2a6;
    line-height: 24px;
    margin: 20px;
    padding-top: 20px;
  }

  .line {
    width: 100%;
    height: 1px;
    background: rgba(0, 0, 0, 0.1);
  }

  .base-info {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    justify-content: space-around;
    width: 100%;
    height: 160px;
  }

  .value {
    text-align: center;
    font-size: 16px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
    line-height: 24px;
  }

  .key {
    margin-top: 8px;
    font-size: 14px;
    font-weight: 400;
    color: rgba(0, 0, 0, 0.65);
    line-height: 24px;
  }

  .changepwd {
    width: calc(100vw - 40px);
    background: #ffffff;
    box-shadow: 0px 0px 16px 0px rgba(0, 0, 0, 0.1);

    .form-inline {
      display: flex;
      justify-content: space-around;
      margin-top: 40px;
      padding-bottom: 20px;

      .input {
        width: 240px;
      }

      /deep/ .el-input__inner {
        border-radius: 20px;
      }
    }
  }

  .logout-container {
    width: 100%;
    text-align: center;

    .logout-button {
      width: 320px;
      height: 40px;
      background: linear-gradient(315deg, #2DA2A6 0%, #B7EB8F 100%);
      border-radius: 20px;
      border: 0;
      margin: 80px auto;
    }
  }
}
</style>
