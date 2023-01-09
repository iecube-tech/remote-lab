<template>
  <div>
    <div>
      <el-form ref="form" :model="form" :rules="rules">
        <el-row :gutter="20" style="margin-bottom: 20px">
          <el-col :span="12">
            <el-card style="height: 580px; margin-bottom: 20px; overflow-y: auto">
              <h4>课程设置</h4>
              <el-form-item label="课程" prop="courseId">
                <el-select v-model="form.courseId" size="small" placeholder="课程" @change="listLesson">
                  <el-option
                    v-for="item in courseList"
                    :key="'teacherCourse' + item.id"
                    :value="item.id"
                    :label="item.name"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="课节" prop="lessonId">
                <el-select v-model="form.lessonId" size="small" placeholder="课节">
                  <el-option
                    v-for="item in lessonList"
                    :key="'teacherLesson' + item.id"
                    :value="item.id"
                    :label="item.name"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="开设日期" prop="dateRange">
                <el-date-picker
                  v-model="form.dateRange"
                  size="small"
                  value-format="yyyy-MM-dd"
                  format="yyyy-MM-dd"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                />
              </el-form-item>
              <el-form-item label="开设时间" prop="timeRange">
                <el-time-picker
                  v-model="form.timeRange"
                  size="small"
                  value-format="HH:mm"
                  format="HH:mm"
                  is-range
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  placeholder="选择时间范围"
                />
              </el-form-item>
              <el-form-item label="单次时长" prop="appointmentDuration">
                <el-input-number v-model="form.appointmentDuration" size="small" />
              </el-form-item>
              <el-form-item label="可预约次数" prop="appointmentCount">
                <el-input-number v-model="form.appointmentCount" size="small" />
              </el-form-item>
              <el-form-item>
                <el-button type="text" @click="toSelectAssistant">选择助教</el-button>
                <div>
                  <el-tag
                    v-if="assistantId && assistantName"
                    :key="assistantId"
                    closable
                    @close="delAssistant"
                  >{{ assistantName }}
                  </el-tag>
                </div>
              </el-form-item>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card style="margin-bottom: 20px; height: 580px; overflow-y: auto">
              <h4>学生设置</h4>
              <div>
                <el-button type="text" @click="toSelectStudent">添加学生</el-button>
              </div>
              <div v-for="item in studentList" :key="'lessonScheduleStudent' + item.id" class="student-card">
                <span class="student-name">{{ item.name }}</span>
              </div>
            </el-card>
          </el-col>

          <el-col :span="24">
            <el-card style="min-height: 800px; margin-bottom: 20px; overflow-y: auto">
              <h4>作业设置</h4>
              <el-form-item prop="homeworkRequire" label="作业要求">
                <tinymce v-model="form.homeworkRequire" style="margin-top: 40px" :height="400" />
              </el-form-item>
              <el-form-item prop="weight" label="加权系数（%）">
                <el-input-number v-model="form.weight" size="small" />
              </el-form-item>
              <el-form-item>
                <div>
                  <el-upload
                    accept=".xls,.xlsx,.pdf,.zip,.jpg,.png"
                    :before-upload="attachmentUpload"
                    action="/"
                  >
                    <el-button class="list-button" type="text">添加附件</el-button>
                  </el-upload>
                  <div
                    v-for="(item, index) in form.homeworkAttachmentList"
                    :key="'homeworkAttachment' + index"
                    class="attachment"
                  >{{ item.filename }}
                  </div>
                </div>
              </el-form-item>
            </el-card>
          </el-col>

          <el-col :span="24">
            <el-card style="margin-bottom: 20px; min-height: 380px">
              <h4>设备设置</h4>
              <el-button type="text" @click="toAddDevice">添加设备</el-button>
              <div>
                <el-row :gutter="20">
                  <el-col
                    v-for="(item, index) in deviceList"
                    :key="'lessonScheduleDevice' + item.id"
                    :span="8"
                    style="margin-top: 20px"
                  >
                    <el-card style="height: 170px">
                      <div slot="header">
                        <span class="device-name">{{ item.deviceName }}</span>
                        <el-button
                          style="float: right; padding: 2px 0"
                          type="text"
                          class="button-text-danger"
                          @click="deleteDevice(index)"
                        ><i class="el-icon-delete" /></el-button>
                      </div>
                      <div style="height: 180px; overflow: hidden; ">
                        <div class="device-id">设备ID：{{ item.deviceId }}</div>
                        <div class="device-location" :title="item.location">设备位置：{{ item.location }}</div>
                      </div>
                    </el-card>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-button type="primary" style="width: 200px" @click="save">保存</el-button>

      </el-form>
    </div>

    <div v-if="showSelectStudentDialog">
      <el-dialog title="添加学生" :visible.sync="showSelectStudentDialog" width="60%">
        <user-select-table
          :type="['STUDENT', 'ASSISTANT']"
          :selected-in="studentList"
          multiple
          @on-confirm="onStudentSelected"
          @on-cancel="showSelectStudentDialog = false"
        />
      </el-dialog>
    </div>

    <div v-if="showSelectAssistantDialog">
      <el-dialog title="选择助教" :visible.sync="showSelectAssistantDialog" width="60%">
        <user-select-table
          :type="['STUDENT', 'ASSISTANT']"
          :selected-in="assistantId ? [{ id: assistantId }] : []"
          @on-confirm="onAssistantSelected"
          @on-cancel="showSelectAssistantDialog = false"
        />
      </el-dialog>
    </div>

    <div v-if="showAddDeviceDialog">
      <el-dialog title="添加设备" :visible.sync="showAddDeviceDialog" width="60%">
        <lesson-schedule-add-device :course-id="form.courseId" @on-confirm="onAddDeviceSuccess" @on-cancel="showAddDeviceDialog = false" />
      </el-dialog>
    </div>

  </div>
</template>

<script>
import UserSelectTable from '@/views/user/UserSelect/UserSelectTable'
import teacherCourseService from '@/service/teacher-course'
import lessonService from '@/service/lesson'
import Tinymce from '@/components/Tinymce'
import LessonScheduleAddDevice from '@/views/lesson-schedule/AddDevice'
import lessonScheduleService from '@/service/lesson-schedule'
import resourceService from '@/service/resource'

export default {
  name: 'LessonScheduleAdd',
  components: { LessonScheduleAddDevice, Tinymce, UserSelectTable },
  data() {
    return {
      routerName: undefined,
      id: undefined,
      courseList: [],
      lessonList: [],
      deviceList: [],
      studentList: [],
      assistantId: undefined,
      assistantName: undefined,
      form: {
        courseId: undefined,
        lessonId: undefined,
        dateRange: undefined,
        timeRange: undefined,
        appointmentDuration: 0,
        appointmentCount: 0,
        homeworkRequire: undefined,
        homeworkAttachmentList: [],
        weight: 100,
        deviceList: [],
        studentIdList: []
      },
      rules: {
        courseId: [
          { required: true, message: '请选择课程' }
        ],
        lessonId: [
          { required: true, message: '请选择课节' }
        ],
        dateRange: [
          { required: true, message: '请选择日期范围' }
        ],
        timeRange: [
          { required: true, message: '请选择每日的时间范围' }
        ],
        appointmentDuration: [
          { required: true, message: '请输入最大单次预约时长' }
        ],
        appointmentCount: [
          { required: true, message: '请输入最大的预约次数' }
        ],
        homeworkRequire: [
          { required: true, message: '请输入作业要求' }
        ],
        weight: [
          { required: true, message: '请输入加权系数' }
        ]
      },
      showSelectStudentDialog: false,
      showSelectAssistantDialog: false,
      showAddDeviceDialog: false
    }
  },
  mounted() {
    this.routerName = this.$route.name
    if (this.$route.name === 'LessonScheduleAdd') {
      this.type = 'ADD'
    } else if (this.$route.name === 'LessonScheduleModify') {
      this.type = 'MODIFY'
      this.id = parseInt(this.$route.params.lessonScheduleId)
    }
    this.fetchData()
  },
  methods: {
    async fetchData() {
      if (this.type === 'MODIFY') {
        await this.get()
      }
      await this.listCourse()
    },
    async get() {
      const { model } = await lessonScheduleService.get(this.id)
      this.form = model
      this.assistantId = model.assistantId
      this.assistantName = model.assistantName
      this.$set(this.form, 'dateRange', [this.form.startDate, this.form.endDate])
      this.$set(this.form, 'timeRange', [this.form.startTime, this.form.endTime])
      this.studentList = this.form.studentList
      this.deviceList = this.form.deviceList
      await this.listLesson()
    },
    async listCourse() {
      const { model } = await teacherCourseService.listGrant({ pageMode: 0 })
      this.courseList = model.content
      if (this.type === 'ADD' && this.$route.query.courseId) {
        this.form.courseId = parseInt(this.$route.query.courseId.toString())
      }
      if (this.form.courseId) {
        await this.listLesson()
      }
    },
    async listLesson() {
      const { model } = await lessonService.listByCourseId(this.form.courseId)
      this.lessonList = model
      if (this.type === 'ADD' && this.$route.query.lessonId) {
        this.form.lessonId = parseInt(this.$route.query.lessonId.toString())
      }
    },
    save() {
      this.$refs['form'].validate(async valid => {
        if (valid) {
          const params = Object.assign({}, this.form)
          params.startDate = this.form.dateRange[0]
          params.endDate = this.form.dateRange[1]
          params.startTime = this.form.timeRange[0]
          params.endTime = this.form.timeRange[1]
          params.deviceList = this.deviceList
          params.studentIds = this.studentList.map(o => o.id)
          params.assistantId = this.assistantId
          params.assistantName = this.assistantName
          params.homeworkAttachmentKeys = this.form.homeworkAttachmentList.map(o => o.key)
          let success
          let message
          try {
            if (this.routerName === 'LessonScheduleAdd') {
              const { code } = await lessonScheduleService.add(params)
              success = code === 0
            } else if (this.routerName === 'LessonScheduleModify') {
              params.id = this.id
              const { code } = await lessonScheduleService.modify(params)
              success = code === 0
            }
          } catch (e) {
            message = e
            console.error(e)
            success = false
          }
          if (success) {
            this.$message.success('操作成功')
            this.$router.go(-1)
          } else {
            this.$message.error(message)
          }
        }
      })
    },
    toSelectStudent() {
      this.showSelectStudentDialog = true
    },
    toSelectAssistant() {
      this.showSelectAssistantDialog = true
    },
    attachmentUpload(file) {
      // const isLt10M = file.size / 1024 / 1024 < 10
      // if (!isLt10M) {
      //   this.$message.error('上传作业大小不能超过 10MB!')
      //   return false
      // }
      const that = this
      this.$confirm(
        '确认上传学生作业吗',
        '操作提示'
      ).then(() => {
        that.upload(file, 'homeworkUrl')
      }).catch(() => {
        that.$message.info('操作取消')
      })
      return false
    },
    async upload(file) {
      const {
        code,
        model
      } = await resourceService.upload(file)
      if (code === 0) {
        this.form.homeworkAttachmentList.push(model)
        console.log(model)
        console.log(this.form.homeworkAttachmentList)
      } else {
        this.$message.error('提交失败')
      }
    },
    toAddDevice() {
      if (!this.form.courseId) {
        this.$message.error('请先选择课程')
        return
      }
      this.showAddDeviceDialog = true
    },
    onStudentSelected(selected) {
      this.showSelectStudentDialog = false
      this.studentList = selected
    },
    onAssistantSelected(selected) {
      this.showSelectAssistantDialog = false
      this.assistantId = selected[0].id
      this.assistantName = selected[0].name
    },
    onAddDeviceSuccess(device) {
      this.deviceList.push(device)
      this.showAddDeviceDialog = false
    },
    deleteDevice(index) {
      this.deviceList.splice(index, 1)
    },
    delAssistant() {
      this.assistantId = undefined
      this.assistantName = undefined
    }
  }
}
</script>

<style scoped>
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

.student-card {
  text-align: center;
  display: inline-block;
  width: 80px;
  height: 40px;
  background: #E6F1FC;
  border: 1px solid #A3D0FD;
  border-radius: 4px;
  padding: 11px;
  margin: 8px 16px 8px 0;
  cursor: pointer;
}

.student-name {
  font-size: 14px;
  color: #1989FA;
  letter-spacing: 0;
  font-weight: 400;
}

.attachment {
  font-size: 14px;
  color: #4A90E2;
  letter-spacing: 0;
  line-height: 24px;
  font-weight: 600;
  cursor: pointer;
}
</style>
