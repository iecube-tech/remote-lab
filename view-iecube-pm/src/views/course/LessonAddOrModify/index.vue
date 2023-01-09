<template>
  <div>
    <div>
      <el-form ref="form" :model="form" label-width="100px" label-position="top">
        <el-row :gutter="40">
          <el-col :span="8">
            <el-form-item label="名称">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="40">
          <el-col :span="8">
            <el-form-item label="课节简介">
              <el-input
                v-model="form.summary"
                class="content-textarea"
                style="height: 178px"
                type="textarea"
                :rows="8"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="课节封面">
              <el-upload
                class="avatar-uploader"
                action="/"
                accept="image/jpeg,image/png"
                :show-file-list="false"
                :before-upload="coverUpload"
              >
                <img v-if="form.coverUrl" :src="'local-resource/' + form.coverUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon" />
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="课节附件">
          <el-upload
            style="width: 500px"
            :action="actionPath"
            multiple
            :headers="uploadHeaders"
            :file-list="form.attachmentList"
            :on-success="onAttachmentUploaded"
            :on-remove="removeAttachmentFile"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item label="课节介绍类型">
          <el-radio-group v-model="form.contentType">
            <el-radio
              v-for="item in lessonContentTypeEnum.enum"
              :key="'lessonContentType' + item.value"
              :label="item.value"
            >
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="form.contentType === 'HTML'" label="课节介绍">
          <tinymce v-model="form.content" :height="400" />
        </el-form-item>
        <el-form-item v-if="form.contentType === 'FILE'" label="正文附件内容地址">
          <el-upload
            style="width: 500px"
            :action="actionPath"
            :headers="uploadHeaders"
            :file-list="contentFile"
            accept=".pdf"
            :on-preview="previewContent"
            :on-change="onContentFileUpload"
            :on-success="onContentUploaded"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传 pdf 文件</div>
          </el-upload>
        </el-form-item>

        <el-form-item label="实验操作页面">
          <el-input style="width: 50%; min-width: 300px" v-model="form.experimentOperationPageUrl" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100px" @click="confirm">确定</el-button>
        </el-form-item>

      </el-form>
    </div>

    <div v-if="showContentPreviewDialog">
      <el-dialog title="预览" :visible.sync="showContentPreviewDialog" width="1000px">
        <pdf-preview :url="'local-resource/' + contentFile[0].url" />
      </el-dialog>
    </div>

  </div>
</template>

<script>
import lessonService from '@/service/lesson'
import resourceService from '@/service/resource'
import Tinymce from '@/components/Tinymce'
import lessonContentTypeEnum from '@/enums/lesson-content-type-enum'
import PdfPreview from '@/components/PdfPreview'

export default {
  name: 'LessonAddOrModify',
  components: { PdfPreview, Tinymce },
  data() {
    return {
      lessonContentTypeEnum,
      courseId: undefined,
      courseName: undefined,
      courseList: [],
      attachmentFileList: [],
      contentFile: [],
      form: {
        name: undefined,
        coverUrl: undefined,
        number: undefined,
        summary: undefined,
        contentType: 'HTML',
        content: undefined,
        contentUrl: undefined,
        courseId: undefined,
        attachmentList: [],
        experimentOperationPageUrl: undefined
      },
      showContentPreviewDialog: false
    }
  },
  computed: {
    actionPath: function() {
      return process.env.VUE_APP_BASE_API + '/resource'
    },
    uploadHeaders: function() {
      return {
        'x-access-token': this.$store.state.user.token,
        'app-code': 'IECUBE_PM'
      }
    }
  },
  mounted() {
    if (this.$route.name === 'LessonAdd') {
      this.type = 'ADD'
    } else if (this.$route.name === 'LessonModify') {
      this.type = 'MODIFY'
      this.id = this.$route.params.lessonId
    }
    this.courseId = this.$route.params.courseId
    this.fetchData()
  },
  methods: {
    fetchData() {
      if (this.type === 'MODIFY' && this.id) {
        this.get()
      }
    },
    async get() {
      const { model } = await lessonService.get(this.id)
      this.form = model
      this.contentFile = [{ name: '正文', url: model.contentUrl }]
      for (let i = 0; i < this.form.attachmentList.length; i++) {
        this.form.attachmentList[i].name = this.form.attachmentList[i].filename
        this.form.attachmentList[i].url = this.form.attachmentList[i].key
      }
    },
    confirm() {
      const params = Object.assign({}, this.form)
      params.courseId = this.courseId
      if (params.attachmentList && params.attachmentList.length > 0) {
        for (let i = 0; i < params.attachmentList.length; i++) {
          params.attachmentList[i].filename = params.attachmentList[i].name
        }
      }
      if (this.type === 'ADD') {
        lessonService.add(params).then(res => {
          this.$message.success('操作成功')
          this.jump('CourseDetail', { params: { courseId: this.form.courseId }})
        }).catch(e => {
          console.error(e)
          this.$message.error(e.data.message)
        })
      } else if (this.type === 'MODIFY') {
        params.id = this.id
        lessonService.edit(params).then(res => {
          this.$message.success('操作成功')
          this.jump('LessonDetail', { params: { courseId: this.form.lessonId }})
        }).catch(e => {
          console.error(e)
          this.$message.error('系统异常')
        })
      }
    },
    coverUpload(file) {
      const isJPG = ['image/jpeg', 'image/png'].includes(file.type)
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是图片格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
        return false
      }
      this.upload(file, 'coverUrl')
      return false
    },
    onAttachmentUploaded(response, file, fileList) {
      this.form.attachmentList.push({
        key: response.model.key,
        url: response.model.key,
        name: response.model.originFilename
      })
    },
    removeAttachmentFile(file, fileList) {
      this.form.attachmentList.splice(this.attachmentFileList.findIndex(o => o.url === file.url), 1)
    },
    onContentUploaded(response, file, fileList) {
      this.form.contentUrl = response.model.key
    },
    onContentFileUpload(file, fileList) {
      this.contentFile = fileList.splice(-1)
    },
    async upload(file, bindKey) {
      const { code, model } = await resourceService.upload(file)
      if (code === 0) {
        this.form[bindKey] = model.key
      }
    },
    jump(routerName, params) {
      this.$router.push({
        name: routerName,
        params: params
      })
    },
    previewContent() {
      this.showContentPreviewDialog = true
    }
  }
}
</script>

<style lang="scss" scoped>
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

.content-textarea {
  ::v-deep .el-textarea {
    height: 200px;
  }
}

</style>
