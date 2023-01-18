<template>
  <div>
    <div v-if="routerName === 'SchoolCourseDetail'" style="padding: 20px">
      <div class="detail-title">课程名称</div>
      <div class="detail-course-name">{{ detail.name }}</div>
      <div class="detail-title">课程简介</div>
      <div class="detail-course-summary">{{ detail.summary }}</div>
      <div class="detail-title">Provider</div>
      <div class="detail-course-provider">{{ detail.organizationName }}</div>
      <div class="detail-title">包含课节</div>
      <div style="margin-bottom: 36px">
        <div
          v-for="item in schoolLessonList"
          :key="'schoolLesson' + item.id"
          class="lesson-card"
          @click="toLessonDetail(item.id)"
        >
          <span class="lesson-name">{{ item.name }}</span>
        </div>
      </div>
      <div class="detail-title">授权</div>
      <div>
        <el-card style="width: 50%">
          <el-tag
            v-for="item in grantUserList"
            :key="'grantTeacher' + item.id"
            style="margin: 0 8px"
            closable
            effect="plain"
            @close="cancelGrant(item.id)"
          >
            {{ item.name }}
          </el-tag>
          <el-button style="margin: 0 8px; padding: 0 16px;" class="button-new-tag" size="small" @click="grant"><span
            style="line-height: 30px"
          >+ 授权</span></el-button>
        </el-card>
      </div>
    </div>
    <router-view />

    <div v-if="showGrantDialog">
      <el-dialog title="授权" :visible.sync="showGrantDialog" width="60%" @closed="showGrantDialog = false">
        <user-select-table
          type="TEACHER"
          :selected-in="grantUserList"
          multiple
          @on-confirm="onSelectConfirm"
          @on-cancel="showGrantDialog = false"
        />
      </el-dialog>
    </div>

  </div>
</template>

<script>
import UserSelectTable from '@/views/user/UserSelect/UserSelectTable'
import schoolCourseService from '@/service/school-course'
import courseService from '@/service/course'
import lessonService from '@/service/lesson'

export default {
  name: 'SchoolCourseDetail',
  components: { UserSelectTable },
  data() {
    return {
      routerName: undefined,
      id: undefined,
      detail: {},
      schoolLessonList: [],
      grantUserList: [],
      showGrantDialog: false
    }
  },
  mounted() {
    if (this.$route.name === 'SchoolCourseDetail') {
      this.routerName = this.$route.name
      this.id = parseInt(this.$route.params.schoolCourseId)
      this.fetchData()
    }
  },
  methods: {
    fetchData() {
      this.get()
      this.listLesson()
      this.listGrantUser()
    },
    async get() {
      const { model } = await courseService.get(this.id)
      this.detail = model
    },
    async listLesson() {
      const { model } = await lessonService.listByCourseId(this.id)
      this.schoolLessonList = model
    },
    async listGrantUser() {
      const { model } = await schoolCourseService.listGrantUser(this.id)
      this.grantUserList = model
    },
    cancelGrant(id) {
      this.$confirm(
        '确认取消授权码？', '操作提示'
      ).then(async() => {
        const { code } = await schoolCourseService.cancelGrant({ courseId: this.id, teacherId: id })
        if (code === 0) {
          this.$message.success('操作成功')
          await this.listGrantUser()
          this.$emit('on-success')
        }
        this.$message.success('操作成功')
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },
    grant() {
      this.showGrantDialog = true
    },
    async onSelectConfirm(selected) {
      const { code } = await schoolCourseService.grant({ courseId: this.id, teacherIds: selected.map(o => o.id) })
      if (code === 0) {
        this.showGrantDialog = false
        this.$message.success('操作成功')
        await this.listGrantUser()
      }
    },
    toLessonDetail(schoolLessonId) {
      this.$router.push({
        name: 'SchoolLessonDetail',
        params: {
          schoolCourseId: this.id,
          schoolLessonId: schoolLessonId
        }
      })
    }
  }
}
</script>

<style scoped>
.detail-title {
  font-size: 16px;
  color: #000000;
  line-height: 16px;
  font-weight: 600;
  margin-bottom: 24px;
}

.detail-course-name {
  font-size: 24px;
  color: #4A90E2;
  line-height: 16px;
  font-weight: 400;
  margin-top: 12px;
  margin-bottom: 56px;
}

.detail-course-summary {
  font-size: 16px;
  color: #999999;
  line-height: 16px;
  font-weight: 400;
  margin-bottom: 36px;
}

.detail-course-provider {
  font-size: 16px;
  color: #999999;
  line-height: 16px;
  font-weight: 400;
  margin-bottom: 36px;
}

.lesson-card {
  display: inline-block;
  width: 320px;
  height: 40px;
  background: #E6F1FC;
  border: 1px solid #A3D0FD;
  border-radius: 4px;
  padding: 11px;
  margin: 8px 16px 8px 0;
  cursor: pointer;
}

.lesson-name {
  font-size: 14px;
  color: #1989FA;
  letter-spacing: 0;
  font-weight: 400;
}
</style>
