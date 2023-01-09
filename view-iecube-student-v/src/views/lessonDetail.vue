<template>
  <div>
    <div>
      <div class="lesson-container">
        <div class="lesson-baseInfo">
          <img v-if="lessonDetail.coverUrl" :src="'local-resource/' + lessonDetail.coverUrl" alt="lesson"
               class="lesson-img">
          <div class="lesson-baseInfo-container">
            <div class="lesson-name">{{ lessonDetail.name }}</div>
            <div class="lesson-summary">{{ lessonDetail.summary }}</div>
            <div class="line"></div>
            <div v-if="lessonDetail.attachmentList && lessonDetail.attachmentList.length > 0">
              附件下载:
              <span v-for="file in lessonDetail.attachmentList" :key="'file' + file.key">
              <span class="lesson-fileName">{{ file.filename }}</span>
              <a :href="'local-resource/' + file.key" :download="file.filename" class="lesson-fileDown">下载</a>
            </span>
            </div>
            <div v-if="schedule && lessonScheduleDetail" class="lesson-personal">
              <div class="lesson-date">
                开放日期：{{ lessonScheduleDetail.startDate }} 至 {{ lessonScheduleDetail.endDate }}
              </div>
              <div class="lesson-operation">
                <el-button type="primary" plain round
                           v-if="(!lessonScheduleDetail) || ( (new Date(lessonScheduleDetail.endDate).getTime() + 24 * 60 * 60 * 1000)  > new Date().getTime())"
                           @click="toAppointment">实验预约
                </el-button>
                <el-button type="primary" plain round @click="submitHomework()" v-if="!(homework && homework.filename)">
                  提交作业
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <div class="lesson-contentInfo">
          <div class="lesson-contentInfo-tittle">课节详情</div>
          <div class="line"></div>
          <div class="lesson-contentInfo-detail">
            <div v-if="lessonDetail.contentType === 'HTML'" v-html="lessonDetail.content"></div>
            <div v-if="lessonDetail.contentType === 'FILE'">
              <pdf-preview :url="'local-resource/' + lessonDetail.contentUrl"/>
            </div>
          </div>
        </div>

        <div v-if="schedule && lessonScheduleDetail" class="lesson-contentInfo">
          <div class="lesson-contentInfo-tittle">作业要求</div>
          <div class="line"></div>
          <div class="lesson-contentInfo-detail">
            <div v-html="homeworkRequire"></div>
            <div v-if="homeworkAttachmentList && homeworkAttachmentList.length > 0">
              附件下载:
              <span v-for="item in homeworkAttachmentList" :key="'homeworkAttachment' + item.key">
              <span
                  style="font-size: 14px; font-weight: 400; color: rgba(0, 0, 0, 0.85); line-height: 24px; margin-right: 20px;">{{
                  item.filename
                }}</span>
              <a :href="'local-resource/' + item.key" :download="item.filename"
                 style="font-size: 14px; font-weight: 400; color: #2DA2A6; line-height: 24px; margin-right: 60px;">下载</a>
            </span>
            </div>
          </div>
        </div>

        <div v-if="schedule && lessonScheduleDetail" class="appointmentAndHomework">
          <div class="appointmentAndHomework-tittle">实验预约和作业详情</div>
          <div class="line"></div>
          <div class="appointmentAndHomework-info">
            <div>
              <div v-if="appointmentList && appointmentList.length > 0">
                <div v-for="appointment in appointmentList"
                     :key="'appointment' + appointment.appointmentDate + ' ' + appointment.startTime">
                  <div class="appointmentAndHomework-detailInfo">预约日期：{{ appointment.appointmentDate }}</div>
                  <div class="appointmentAndHomework-detailInfo">
                    预约时间：{{ appointment.startTime }} ~ {{ appointment.endTime }}
                  </div>
                  <div class="appointmentAndHomework-detailInfo">
                    预约设备：{{ appointment.deviceOperatingDTO && appointment.deviceOperatingDTO.deviceName }}
                  </div>
                  <el-button
                      :type="appointment.buttonType"
                      plain
                      round
                      @click="deviceControl(appointment)"
                      :disabled="appointment.appointmentStatus !== 'IN_PROGRESS'"
                      class="appointmentAndHomework-deviceControl">
                    <span v-if="appointment.appointmentStatus === 'NOT_START'">
                      <span v-if="appointment.gapMin <= 5" style="color: brown">
                        {{ appointment.gapMin }} : {{ appointment.gapSec }}</span>
                      <span v-else>未开始</span>
                    </span>
                    <span v-if="appointment.appointmentStatus === 'IN_PROGRESS'">操作设备</span>
                    <span v-if="appointment.appointmentStatus === 'FINISHED'">已结束</span>
                  </el-button>
                  <el-button
                      v-if="appointment.appointmentStatus === 'NOT_START'"
                      plain round @click="cancelAppointment(appointment)"
                      class="appointmentAndHomework-deviceControl">取消预约
                  </el-button>
                </div>
              </div>
              <div v-else class="appointmentAndHomework-detailInfo">暂无预约信息</div>
              <div v-if="homework && homework.filename" class="appointmentAndHomework-detailInfo">
                课节作业：{{ homework.filename }}
                <span v-if="new Date(lessonScheduleDetail.startDate).getTime() <= new Date().getTime()
                && new Date(lessonScheduleDetail.endDate).getTime() >= new Date().getTime()"
                      class="appointmentAndHomework-operation" @click="submitHomework">
                  重新上传
                </span>
              </div>
              <div style="margin-bottom: 20px" v-else>暂无作业信息</div>
            </div>

            <span v-if="studentLessonScheduleDetail.score" class="lesson-score">{{ studentLessonScheduleDetail.score }}
              <span class="lesson-score-word">分</span>
            </span>
          </div>

        </div>

        <div class="lesson-commentInfo">
          <div class="lesson-commentInfo-tittle">留言区</div>
          <div class="line"></div>
          <el-form ref="commentForm" :model="commentForm">
            <el-form-item>
              <el-input type="textarea" v-model="commentForm.content" class="lesson-commentInfo-textarea"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="toComment" class="lesson-commentInfo-button">提交留言</el-button>
            </el-form-item>
          </el-form>
          <div v-for="item in comment" :key="'comment' + item.id" class="comment-area">
            <div class="lesson-commentInfo-content"> {{ item.content }}</div>
            <span class="lesson-commentInfo-creatorName"> {{ item.creatorName }}</span>
            <span class="lesson-commentInfo-nomalInfo"> {{ item.createTime | datetime }}</span>
            <span v-if="item.top" class="lesson-commentInfo-nomalInfo comment-top">置顶</span>
            <div v-if="item.reply" class="lesson-replyInfo">
              <div class="lesson-commentInfo-content"> {{ item.reply.content }}</div>
              <span class="lesson-commentInfo-creatorName"> {{ item.reply.creatorName }}</span>
              <span class="lesson-commentInfo-createTime"> {{ item.reply.createTime | datetime }}</span>
            </div>
            <div class="line"></div>
          </div>
        </div>

      </div>

    </div>

    <div v-if="appointFormVisible">
      <el-dialog title="实验预约" :visible.sync="appointFormVisible" class="appointForm">
        <el-form :model="appointForm">
          <el-form-item label="预约日期">
            <el-date-picker type="date"
                            value-format="yyyy-MM-dd"
                            :picker-options="{ disabledDate: disabledDate }"
                            placeholder="请选择日期"
                            v-model="appointForm.appointmentDate"
                            @change="dateChange"
                            class="date-picker"></el-date-picker>
          </el-form-item>
          <el-form-item label="选择设备">
            <el-radio-group v-model="appointForm.deviceId"
                            @change="deviceChange">
              <el-radio v-for="device in lessonScheduleDetail.deviceList" :key="'device' + device.id"
                        :label="device.id"
                        class="device-picker"> {{ device.deviceName }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="选择课时">
            <el-select v-model="appointForm.timeRange"
                       :placeholder="(appointForm.deviceId && appointForm.appointmentDate) ? '请选择课时' : '请先选择日期和设备'"
                       class="time-picker">
              <el-option
                  v-for="item in appointOptions"
                  :key="'timePeriod' +item.startTime + '-' + item.endTime"
                  :label="item.startTime + '-' + item.endTime"
                  :value="item.startTime + '-' + item.endTime"
                  :disabled="!item.status">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitAppointment">提交预约</el-button>
          <el-button @click="appointFormVisible = false">取消</el-button>
        </div>
      </el-dialog>
    </div>

    <div v-if="homeworkFormVisible">
      <el-dialog title="提交作业" :visible.sync="homeworkFormVisible">
        <el-form ref="form" :model="homeworkForm" label-width="100px" label-position="top">
          <el-upload
              class="upload-demo"
              drag
              action="/"
              accept="pdf,zip"
              :show-file-list="false"
              :before-upload="homeworkUpload">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传 pdf/zip 文件，且不超过 10MB</div>
          </el-upload>
        </el-form>
      </el-dialog>
    </div>

  </div>
</template>

<script>
import commentService from '@/service/comment'
import lessonService from '@/service/lesson'
import studentCourseService from '@/service/student-course'
import appointmentService from '@/service/appointment'
import resourceService from '@/service/resource'
import homeworkService from '@/service/homework'
import PdfPreview from '@/components/pdf-preview'

export default {
  name: 'LessonDetail',
  components: { PdfPreview },
  data () {
    return {
      type: 'public',
      schedule: false,
      courseId: undefined,
      lessonId: undefined,
      lessonScheduleId: undefined,
      courseCollection: false,
      homeworkAttachmentList: [{
        filename: '',
        key: ''
      }],
      homework: {
        filename: '',
        key: ''
      },
      lessonScheduleDetail: {},
      lessonDetail: {},
      appointmentList: [],
      commentForm: {
        content: undefined
      },
      studentLessonScheduleDetail: {
        appointmentCount: undefined,
        appointmentDuration: undefined,
        id: undefined,
        lessonScheduleId: undefined,
        score: undefined
      },
      comment: [],
      appointFormVisible: false,
      homeworkFormVisible: false,
      appointForm: {
        appointmentDate: undefined,
        timeRange: undefined,
        deviceId: undefined
      },
      homeworkRequire: undefined,
      weight: undefined,
      appointOptions: [],
      deviceList: [],
      homeworkForm: {
        key: '',
        lessonScheduleId: undefined
      }
    }
  },
  watch: {
    '$route' (to, from) {
      this.init()
      this.fetchData()
    }
  },
  mounted () {
    this.init()
    this.fetchData()
  },
  methods: {
    init () {
      this.courseId = parseInt(this.$route.params.courseId)
      this.lessonId = parseInt(this.$route.params.lessonId)
      if (this.$route.name === 'LessonDetail') {
        this.type = 'public'
        this.schedule = false
      } else if (this.$route.name === 'LessonScheduleDetail') {
        this.lessonScheduleId = parseInt(this.$route.params.lessonScheduleId)
        this.type = 'schedule'
        this.schedule = true
      } else if (this.$route.name === 'LessonSchedulePublicLesson') {
        this.lessonScheduleId = undefined
        this.type = 'schedule'
        this.schedule = false
      }
    },
    fetchData () {
      try {
        this.getLessonDetail()
        this.listComment()
      } catch (e) {
        console.error(e)
        this.$message.error(e.message)
      }
    },
    async getLessonDetail () {
      const { model } = await lessonService.get(this.lessonId)
      this.lessonDetail = model
      if (this.schedule) {
        const { model: lessonSchedule } = await studentCourseService.getLessonSchedule(this.lessonScheduleId)
        this.lessonScheduleDetail = lessonSchedule
        this.lessonScheduleId = lessonSchedule.id
        this.studentLessonScheduleDetail = lessonSchedule.studentLessonScheduleDetail
        this.homeworkAttachmentList = lessonSchedule.homeworkAttachmentList
        this.homework = lessonSchedule.homework
        this.appointmentList = lessonSchedule.appointmentList
        this.weight = lessonSchedule.weight
        this.homeworkRequire = lessonSchedule.homeworkRequire
        if (this.appointmentList && this.appointmentList.length > 0) {
          this.processAppointment()
        }
      }
    },
    async listComment () {
      const { model } = await commentService.list({ lessonId: this.lessonId, pageMode: 0 })
      this.comment = model.content
    },
    processAppointment () {
      const that = this
      that.processAppointmentStatus(that)
      setInterval(() => {
        that.processAppointmentStatus(that)
      }, 1000)
    },
    processAppointmentStatus (that) {
      for (let i = 0; i < this.appointmentList.length; i++) {
        const now = new Date()
        const appointment = this.appointmentList[i]
        const start = new Date(appointment.appointmentDate + ' ' + appointment.startTime)
        const end = new Date(appointment.appointmentDate + ' ' + appointment.endTime)
        if (start.getTime() > now.getTime()) {
          that.$set(this.appointmentList[i], 'appointmentStatus', 'NOT_START')
          const ms = start.getTime() - new Date().getTime()
          const min = ms / 1000 / 60
          const sec = ms / 1000 % 60
          that.$set(this.appointmentList[i], 'gapMin', parseInt(min.toString()) < 0 ? 0 : parseInt(min.toString()))
          that.$set(this.appointmentList[i], 'gapSec', parseInt(sec.toString()) < 0 ? 0 : parseInt(sec.toString()))
          if (this.appointmentList[i].gapMin <= 5) {
            that.$set(this.appointmentList[i], 'buttonType', 'warning')
          } else {
            that.$set(this.appointmentList[i], 'buttonType', 'default')
          }
        } else if (start.getTime() <= now.getTime() && end.getTime() >= now.getTime()) {
          that.$set(this.appointmentList[i], 'appointmentStatus', 'IN_PROGRESS')
          that.$set(this.appointmentList[i], 'buttonType', 'primary')
        } else {
          that.$set(this.appointmentList[i], 'appointmentStatus', 'FINISHED')
          that.$set(this.appointmentList[i], 'buttonType', 'info')
        }
      }
    },
    toAppointment () {
      this.appointFormVisible = true
    },
    async cancelAppointment (appointment) {
      this.$confirm(
        '确认取消预约吗？',
        '操作提示'
      ).then(async () => {
        try {
          const params = Object.assign({}, appointment)
          const { code } = await appointmentService.cancel(params)
          if (code === 0) {
            this.$message.success('取消预约成功')
            await this.getLessonDetail()
            this.appointFormVisible = false
          }
        } catch (e) {
          console.error(e)
          this.$message.error(e.message)
        }
      }).catch(() => {
        this.$message.info('操作取消')
      })
    },
    submitHomework () {
      this.homeworkFormVisible = true
    },

    deviceControl (appointment) {
      console.log(appointment)
      this.$router.push({
        name: 'deviceControl',
        params: {
          lessonScheduleId: appointment.lessonScheduleId,
          date: appointment.appointmentDate,
          startTime: appointment.startTime,
          endTime: appointment.endTime,
          deviceId: appointment.deviceId
        }
      })
    },
    toComment () {
      this.$confirm('确认提交吗？', '提示').then(async () => {
        this.commentForm.lessonId = this.lessonDetail.id
        const {
          code,
          message
        } = await commentService.add(this.commentForm)
        if (code === 0) {
          await this.fetchData()
          this.commentForm.content = ''
          this.$message.success('操作成功')
        } else {
          this.$message.error(message)
        }
      }).catch(e => {
        console.error(e)
        this.$message.info('操作取消')
      })
    },
    deviceChange (device) {
      if (this.appointForm.deviceId && this.appointForm.appointmentDate) {
        this.listTimePeriod()
      }
    },
    dateChange (date) {
      if (this.appointForm.deviceId && this.appointForm.appointmentDate) {
        this.listTimePeriod()
      }
    },
    async submitAppointment () {
      try {
        const params = Object.assign({}, this.appointForm)
        params.startTime = params.timeRange.split('-')[0]
        params.endTime = params.timeRange.split('-')[1]
        const { code } = await appointmentService.appointment(params)
        if (code === 0) {
          this.appointForm = this.$options.data().appointForm
          this.$message.success('预约成功')
          await this.getLessonDetail()
          this.appointFormVisible = false
        }
      } catch (e) {
        console.error(e)
        this.$message.error(e.message)
      }
    },
    async listTimePeriod () {
      this.appointForm.lessonScheduleId = this.lessonScheduleDetail.id
      const { model } = await appointmentService.listTimePeriod(this.appointForm)
      this.appointOptions = model
      for (let i = 0; i < this.appointOptions.length; i++) {
        const item = this.appointOptions[i]
        const start = new Date(item.appointmentDate + ' ' + item.startTime)
        if (start.getTime() <= new Date().getTime()) {
          this.$set(this.appointOptions[i], 'status', false)
        }
      }
    },
    disabledDate (date) {
      const now = new Date()
      const d = new Date(now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate())
      // console.log(new Date(this.lessonScheduleDetail.endDate).getTime())
      // console.log(new Date(this.lessonScheduleDetail.startDate).getTime())
      // console.log(new Date(this.lessonScheduleDetail.startDate).getTime() - 24 * 60 * 60 * 1000)
      // console.log(date.getTime())
      return date.getTime() < new Date(this.lessonScheduleDetail.startDate).getTime() - 24 * 60 * 60 * 1000 || 
          date.getTime() > new Date(this.lessonScheduleDetail.endDate).getTime() || date.getTime() < d.getTime() - 24 * 60 * 60 * 1000
          // date.getTime() < Date.now() ||
          // date.getTime() < d.getTime()
          // date.getTime() < new Date(this.lessonScheduleDetail.startDate).getTime() ||
          // date.getTime() > new Date(this.lessonScheduleDetail.endDate).getTime() 
          // date.getTime() <= d.getTime()
    },
    homeworkUpload (file) {
      const isRightFile = ['application/pdf', 'application/zip', 'application/x-zip-compressed'].includes(file.type)
      // const isLt10M = file.size / 1024 / 1024 < 10
      if (!isRightFile) {
        this.$message.error('上传作业只能是 PDF 或 ZIP 格式!')
        return false
      }
      // if (!isLt10M) {
      //   this.$message.error('上传作业大小不能超过 10MB!')
      //   return false
      // }
      const that = this
      this.$confirm(
        '确认上传作业吗',
        '操作提示'
      ).then(() => {
        that.upload(file, 'homeworkUrl')
      }).catch(() => {
        that.$message.info('操作取消')
      })
      return false
    },
    async upload (file) {
      try {
        const {
          code,
          model
        } = await resourceService.upload(file)
        if (code === 0) {
          this.homeworkForm.key = model.key
          this.homeworkForm.lessonScheduleId = this.studentLessonScheduleDetail.lessonScheduleId
          try {
            const { code } = await homeworkService.submit(this.homeworkForm)
            if (code === 0) {
              await this.getLessonDetail()
              this.homeworkFormVisible = false
              this.$message.success('提交成功')
            } else {
              this.$message.error('提交失败')
              this.homeworkFormVisible = false
            }
          } catch (e) {
            this.$message.error(e)
            this.homeworkFormVisible = false
          }
        }
      } catch (e) {
        this.$message.error(e)
        this.homeworkFormVisible = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>

.body-container {
  margin-top: 60px;
  font-size: 14px;
  overflow-x: hidden;

  .course-menu {
    height: calc(100vh - 60px);
    width: 320px;
    background-color: #fff;
    position: fixed;
    top: 60px;

    .course-base-info {
      margin-top: 40px;
      margin-left: 20px;
      margin-right: 20px;

      .tittle-info {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .courese-titte {
          font-size: 16px;
          font-weight: 600;
          color: rgba(0, 0, 0, 0.85);
          line-height: 24px;
        }
      }

      .courseSummary {
        margin-top: 20px;
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.65);
        line-height: 24px;
      }
    }

    .line {
      margin-top: 20px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
    }

    .lesson-tips {
      margin-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: rgba(0, 0, 0, 0.65);
      line-height: 24px;
    }

    .lesson-list-container {
      display: flex;
      align-items: center;
      margin-top: 10px;
      width: 320px;
      height: 40px;
      color: rgba(0, 0, 0, 0.65);
      cursor: pointer;

      .lesson-staus-icon {
        margin-left: 20px;
        width: 20px;
        height: 20px;
      }

      .lesson-tittle {
        margin-left: 20px;
      }
    }

  }

  .lesson-container {
    margin-left: 340px;
    width: calc(100vw - 360px);
    position: relative;
    top: 20px;

    .lesson-baseInfo {
      background-color: #fff;
    }

    .appointmentAndHomework, .lesson-contentInfo, .lesson-commentInfo {
      background-color: #fff;
      margin-top: 20px;
    }

    .lesson-baseInfo {
      display: flex;
      align-items: center;

      .lesson-img {
        width: 400px;
        height: 240px;
      }

      .lesson-baseInfo-container {
        margin-left: 20px;
        margin-right: 20px;

        .lesson-name {
          margin-top: 20px;
          font-size: 20px;
          font-weight: 600;
          color: #2DA2A6;
          line-height: 30px;
        }

        .lesson-summary {
          margin-top: 12px;
          font-size: 14px;
          font-weight: 400;
          color: rgba(0, 0, 0, 0.65);
          line-height: 24px;
        }

        .line {
          margin-top: 12px;
          width: 100%;
          height: 1px;
          background-color: rgba(0, 0, 0, 0.1);
          margin-bottom: 12px;
        }

        .lesson-fileName {
          font-size: 14px;
          font-weight: 400;
          color: rgba(0, 0, 0, 0.85);
          line-height: 24px;
          margin-right: 20px;
        }

        .lesson-fileDown {
          font-size: 14px;
          font-weight: 400;
          color: #2DA2A6;
          line-height: 24px;
          margin-right: 60px;
        }

        .lesson-personal {
          margin-top: 20px;

          .lesson-date {
            font-size: 14px;
            font-weight: 400;
            color: rgba(0, 0, 0, 0.85);
            line-height: 20px;
            margin-bottom: 20px;
          }

          .lesson-operation {
            margin-bottom: 20px;
          }
        }
      }

    }
  }

  .appointmentAndHomework {

    .appointmentAndHomework-tittle {
      padding-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: #2DA2A6;
      line-height: 24px;
    }

    .line {
      margin-top: 12px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
      margin-bottom: 12px;
    }

    .appointmentAndHomework-info {
      display: flex;
      justify-content: space-between;
      margin-left: 20px;
      margin-right: 20px;

      .appointmentAndHomework-deviceControl {
        margin-bottom: 40px;
      }

      .appointmentAndHomework-detailInfo {
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.85);
        line-height: 24px;
        margin-bottom: 20px;

        .appointmentAndHomework-operation {
          font-size: 14px;
          font-weight: 400;
          color: #2DA2A6;
          line-height: 24px;
          margin-left: 40px;
          cursor: pointer;
        }
      }

      .lesson-score {
        font-size: 48px;
        font-weight: bold;
        color: #FFA940;
        line-height: 72px;

        .lesson-score-word {
          font-size: 16px;
          line-height: 24px;
          margin-left: -12px;
        }
      }
    }
  }

  .lesson-contentInfo {

    .lesson-contentInfo-tittle {
      padding-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: #2DA2A6;
      line-height: 24px;
    }

    .line {
      margin-top: 12px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
      margin-bottom: 12px;
    }

    .lesson-contentInfo-detail {
      font-size: 14px;
      font-weight: 400;
      color: rgba(0, 0, 0, 0.85);
      line-height: 24px;
      margin: 20px;
      padding-bottom: 20px;
    }
  }

  .lesson-commentInfo {

    .lesson-commentInfo-tittle {
      padding-top: 20px;
      margin-left: 20px;
      font-size: 16px;
      font-weight: 400;
      color: #2DA2A6;
      line-height: 24px;
    }

    .line {
      margin-top: 12px;
      width: 100%;
      height: 1px;
      background-color: rgba(0, 0, 0, 0.1);
      margin-bottom: 12px;
    }

    .lesson-commentInfo-textarea {
      margin-left: 20px;
      margin-right: 20px;
      margin-top: 12px;

      /deep/ .el-textarea__inner {
        width: calc(100% - 40px);
        height: 80px;
      }
    }

    .lesson-commentInfo-button {
      margin-left: 20px;
      margin-bottom: 20px;
    }

    .comment-area {
      margin: 20px;

      .lesson-commentInfo-content {
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.85);
        line-height: 24px;
        margin-bottom: 12px;
      }

      .lesson-commentInfo-creatorName {
        font-size: 14px;
        font-weight: 400;
        color: #2DA2A6;
        line-height: 24px;
      }

      .lesson-commentInfo-nomalInfo {
        font-size: 14px;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.65);
        line-height: 24px;
        margin-left: 40px;
      }

      .comment-top {
        right: 40px;
        position: absolute;
      }

      .lesson-replyInfo {
        background-color: #E6FFFB;
        margin-top: 8px;
        padding-left: 20px;
        padding-right: 20px;
        padding-top: 12px;
        padding-bottom: 12px;
      }

      .line {
        margin-top: 12px;
        width: 100%;
        height: 1px;
        background-color: rgba(0, 0, 0, 0.1);
        margin-bottom: 12px;
      }
    }

  }
}

.appointForm {

  .date-picker {
    width: 320px;
  }

  .time-picker {
    width: 320px;
  }

  .device-picker {
    margin-right: 30px;
  }

  .dialog-footer {
    text-align: left;
  }
}

.upload-demo {
  text-align: center;
  margin-top: 40px;
}
</style>
