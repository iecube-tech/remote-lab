<template>
  <div>
    <div>
      <div class="info-item">姓名：{{ data.creatorName }}</div>
      <div class="info-item">账号：{{ data.email }}</div>
      <div class="info-item">是否置顶：{{ data.top | topStatusFilter }}</div>
      <div class="info-item">留言时间：{{ data.createTime }}</div>
      <div class="info-item">留言内容：{{ data.content }}</div>
    </div>
    <div style="margin-top: 20px">
      <el-form ref="form" :model="form" label-position="top">
        <el-form-item :rules="[ { required: true, message: '回复内容不能为空' } ]" label="回复">
          <el-input v-model="form.content" type="textarea" :rows="4"/>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="reply(data.id)">回复</el-button>
    </div>
  </div>
</template>

<script>
import topStatusEnum from '@/enums/comment-top-status-enum'
import commentService from '@/service/comment'

export default {
  name: 'CommentDetail',
  filters: {
    topStatusFilter: topStatusEnum.filter
  },
  props: {
    id: {
      type: Number,
      default: undefined
    }
  },
  data() {
    return {
      topStatusEnum,
      data: {
        email: undefined,
        creatorId: undefined,
        creatorName: undefined
      },
      form: {
        commentId: undefined,
        content: undefined
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.get()
    },
    async get() {
      const { model } = await commentService.get(this.id)
      this.data = model
      this.form = model.reply || {}
    },
    reply(commentId) {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.$confirm(
            '确认回复该内容吗？',
            '操作提示'
          ).then(async() => {
            this.form.commentId = commentId
            const { code } = await commentService.reply(this.form)
            if (code === 0) {
              this.$message.success('操作成功')
              this.$emit('on-success')
            }
          }).catch(() => {
            this.$message.info('操作取消')
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.info-item {
  line-height: 24px;
}
</style>
