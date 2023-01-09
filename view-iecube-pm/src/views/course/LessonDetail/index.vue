<template>
  <div>
    <div class="lesson-header">
      <img v-if="data.coverUrl" width="72px" height="72px" :src="'local-resource/' + data.coverUrl" alt="">
      <span class="lesson-name">{{ data.name }}</span>
    </div>
    <div>
      <el-row :gutter="20">
        <el-col :span="18">
          <el-card style="min-height: 400px">
            <div v-if="data.contentType === 'HTML'" v-html="data.content" />
            <div v-if="data.contentType === 'FILE' && data.contentUrl">
              <pdf-preview :url="'local-resource/' + data.contentUrl" />
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="margin-bottom: 20px">
            <div class="card-title">所属课程</div>
            <div class="course-name">{{ data.courseName }}</div>
          </el-card>
          <el-card style="margin-bottom: 20px">
            <div class="card-title">课程简介</div>
            <div class="course-summary">{{ data.summary }}</div>
          </el-card>
          <el-card>
            <div class="card-title">课节附件</div>
            <div v-for="(item, index) in data.attachmentList" :key="'attachment' + index" class="course-attachment">
              <a target="_blank" :href="'local-resource/' + item.key">{{ item.filename }}</a>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
import lessonService from '@/service/lesson'
import PdfPreview from '@/components/PdfPreview'

export default {
  name: 'LessonDetail',
  components: { PdfPreview },
  data() {
    return {
      id: undefined,
      courseId: undefined,
      data: {}
    }
  },
  mounted() {
    this.id = parseInt(this.$route.params.lessonId)
    this.courseId = parseInt(this.$route.params.courseId)
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.get()
    },
    async get() {
      const { model } = await lessonService.get(this.id)
      this.data = model
    }
  }
}
</script>

<style scoped>
.lesson-name {
  display: inline-block;
  font-size: 36px;
  color: #4A90E2;
  font-weight: 600;
  line-height: 72px;
  margin-left: 24px;
}

.lesson-header {
  border: 1px solid #979797;
  margin-bottom: 20px;
}

.lesson-header * {
  display: inline-block;
  vertical-align: middle;
}

.card-title {
  font-size: 16px;
  color: #303133;
  letter-spacing: 0;
  line-height: 16px;
  font-weight: 600;
  margin-bottom: 12px;
}

.course-name {
  font-size: 14px;
  color: #4A90E2;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 600;
}

.course-summary {
  font-size: 14px;
  color: #4A4A4A;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 400;
}

.course-attachment {
  font-size: 14px;
  color: #4A90E2;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 600;
  cursor: pointer;
  overflow: hidden;
}
</style>
