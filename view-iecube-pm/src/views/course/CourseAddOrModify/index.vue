<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" label-width="100px" label-position="top">
      <el-form-item label="课程名称" style="margin-bottom: 40px">
        <el-input v-model="form.name" style="width: 500px" />
      </el-form-item>
      <el-form-item label="课程封面" style="margin-bottom: 40px">
        <el-upload
          class="avatar-uploader"
          action="/"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="form.coverUrl" :src="'local-resource/' + form.coverUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
      </el-form-item>
      <el-form-item label="课程简介" style="margin-bottom: 40px">
        <el-input v-model="form.summary" type="textarea" :rows="5" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirm">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import courseService from '@/service/course'
import resourceService from '@/service/resource'

export default {
  name: 'CourseAddOrModify',
  data() {
    return {
      id: undefined,
      type: 'ADD',
      form: {
        name: undefined,
        coverUrl: undefined,
        summary: undefined
      }
    }
  },
  mounted() {
    if (this.$route.name === 'CourseAdd') {
      this.type = 'ADD'
    } else if (this.$route.name === 'CourseModify') {
      this.type = 'MODIFY'
      this.id = parseInt(this.$route.params.courseId)
    }
    this.fetchData()
  },
  methods: {
    fetchData() {
      if (this.type === 'MODIFY' && this.id) {
        this.get()
      }
    },
    async get() {
      const { model } = await courseService.get(this.id)
      this.form = model
    },
    confirm() {
      const params = Object.assign({}, this.form)
      if (this.type === 'ADD') {
        courseService.add(params).then(res => {
          this.$router.push({ name: 'CourseList' })
        }).catch(e => {
          console.error(e)
          this.$message.error('操作异常')
        })
      } else if (this.type === 'MODIFY') {
        params.id = this.id
        courseService.edit(params).then(res => {
          this.$emit('on-success')
          this.$router.push({ name: 'CourseDetail' })
        }).catch(e => {
          console.error(e)
          this.$message.error('操作异常')
        })
      }
    },
    beforeAvatarUpload(file) {
      resourceService.upload(file).then(res => {
        if (res.code === 0) {
          this.form.coverUrl = res.model.key
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
