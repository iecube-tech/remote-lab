<template>
  <div>
    <div>
      <el-form>
        <el-row :gutter="20" style="margin-bottom: 20px">
          <el-col :span="12">
            <el-card style="height: 550px">
              <h4>基础设置</h4>
              <el-form-item label="课程">
                {{ data.courseName }}
              </el-form-item>
              <el-form-item label="课节">
                {{ data.lessonName }}
              </el-form-item>
              <el-form-item label="开设日期">
                {{ data.startDate }} - {{ data.endDate }}
              </el-form-item>
              <el-form-item label="开设时间">
                {{ data.startTime }} - {{ data.endTime }}
              </el-form-item>
              <el-form-item label="单次时长">
                {{ data.appointmentDuration }}
              </el-form-item>
              <el-form-item label="可预约次数">
                {{ data.appointmentCount }}
              </el-form-item>
              <el-form-item label="可预约时间段限制">
                {{ data.dayLimit }}
              </el-form-item>
              <el-form-item v-if="data.assistantId && data.assistantName" label="助教">
                {{ data.assistantName }}
              </el-form-item>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card style="margin-bottom: 20px; height: 550px">
              <h4>学生设置</h4>
              <div v-for="item in data.studentList" :key="'lessonScheduleStudent' + item.id" class="student-card">
                <span class="student-name">{{ item.name }}</span>
              </div>
            </el-card>
          </el-col>
          <el-col :span="24">
            <el-card style="max-height: 800px; margin-bottom: 20px">
              <h4>作业设置</h4>
              <el-form-item label="作业要求">
                <div style="margin-top: 40px" v-html="data.homeworkRequire" />
              </el-form-item>
              <el-form-item label="加权系数">
                {{ data.weight }}
              </el-form-item>
              <el-form-item label="附件">
                <div style="margin-top: 40px">
                  <div v-for="(item, index) in data.homeworkAttachmentList" :key="'homeworkAttachment' + index"
                    class="attachment">{{ item.filename }}</div>
                </div>
              </el-form-item>
            </el-card>
          </el-col>
          <el-col :span="24">
            <el-card style="margin-bottom: 20px; min-height: 380px">
              <h4>设备设置</h4>
              <div>
                <el-row :gutter="20">
                  <el-col v-for="item in data.deviceList" :key="'lessonScheduleDevice' + item.id" :span="8"
                    style="margin-top: 20px">
                    <el-card style="height: 120px">
                      <div style="overflow: hidden; ">
                        <div class="device-id">设备ID：{{ item.deviceId }}</div>
                        <div class="device-location">设备位置：{{ item.location }}</div>
                      </div>
                    </el-card>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
import lessonScheduleService from '@/service/lesson-schedule'

export default {
  name: 'LessonScheduleDetail',
  data() {
    return {
      id: undefined,
      data: {}
    }
  },
  mounted() {
    this.id = this.$route.params.lessonScheduleId
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.get()
    },
    async get() {
      const { model } = await lessonScheduleService.get(this.id)
      this.data = model
    }
  }
}
</script>

<style scoped>
.attachment {
  font-size: 14px;
  color: #4A90E2;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 600;
  cursor: pointer;
}

.device-name {
  font-size: 16px;
  color: #303133;
  letter-spacing: 0;
  line-height: 18px;
  font-weight: 600;
}

.device-id {
  margin-bottom: 10px;
  font-size: 14px;
  color: #606266;
  letter-spacing: 0;
  line-height: 14px;
  font-weight: 400;
}

.device-location {
  margin-bottom: 4px;
  font-size: 14px;
  color: #606266;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 400;
}

.device-explain {
  font-size: 14px;
  color: #606266;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 400;
  padding-right: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
