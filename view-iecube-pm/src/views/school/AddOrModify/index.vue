<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" label-width="100px">
      <el-form-item label="名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="管理员名称">
        <el-input v-model="form.adminName" />
      </el-form-item>
      <el-form-item label="管理员邮箱">
        <el-input v-model="form.adminEmail" />
      </el-form-item>
      <el-form-item label="Logo">
        <el-upload
          class="avatar-uploader"
          action="/"
          :show-file-list="false"
          :before-upload="beforeLogoUpload"
        >
          <img v-if="form.logoUrl" :src="'local-resource/' + form.logoUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
      </el-form-item>
      <el-form-item label="管理端 Logo">
        <el-upload
          class="avatar-uploader"
          action="/"
          :show-file-list="false"
          :before-upload="beforeAdminLogoUpload"
        >
          <img v-if="form.adminLogoUrl" :src="'local-resource/' + form.adminLogoUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
      </el-form-item>
      <el-form-item label="状态">
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
        <el-button type="primary" :loading="loading" @click="confirm">保存</el-button>
        <el-button @click="closeDialog">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import schoolService from '@/service/school'
import enableStatusEnum from '@/enums/enable-status-enum'
import resourceService from '@/service/resource'

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
      enableStatusEnum,
      form: {
        name: undefined,
        adminName: undefined,
        adminEmail: undefined,
        sld: undefined,
        homePageBackgroundUrl: undefined,
        logoUrl: undefined,
        adminLogoUrl: undefined,
        status: undefined
      },
      loading: false
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      if (this.type === 'MODIFY' && this.id) {
        this.get()
      }
    },
    async get() {
      const { model } = await schoolService.get(this.id)
      this.form = model
    },
    async confirm() {
      this.loading = true
      try {
        const params = Object.assign({}, this.form)
        if (this.type === 'ADD') {
          await schoolService.add(params)
        } else if (this.type === 'MODIFY') {
          params.id = this.id
          await schoolService.edit(params)
        }
        this.$emit('on-success')
        this.$message.success('操作成功')
        this.loading = false
      } catch (e) {
        console.error(e)
        this.$message.error(e.message || '系统异常')
        this.loading = false
      }
    },
    // 取消点击 关闭弹层
    closeDialog() {
      this.$emit('on-cancel')
    },
    beforeLogoUpload(file) {
      resourceService.upload(file).then(res => {
        if (res.code === 0) {
          this.form.logoUrl = res.model.key
        }
      })
      return false
    },
    beforeAdminLogoUpload(file) {
      resourceService.upload(file).then(res => {
        if (res.code === 0) {
          this.form.adminLogoUrl = res.model.key
        }
      })
      return false
    }
  }
}
</script>

<style scoped>
.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
