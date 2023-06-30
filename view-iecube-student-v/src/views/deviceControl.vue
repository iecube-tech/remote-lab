<template>
  <div class="view">
    <el-container>
      <el-aside width="480px" class="view-aside" v-if="asideVisible">
        <el-row class="aside-tittle">
          <el-button type="text" @click="goback()"> {{ '<返回' }} </el-button>
        </el-row>
        <div class="aside-tittle">
          <span class="lessonDetailName">{{ deviceOperating.lessonName }}</span>
          <!-- <i class="el-icon-s-fold" style="cursor: pointer" @click="putAway()"></i> -->
        </div>

        <div class="view-instruction">
          <div class="over" v-if="residueMin === 0 && residueSec === 0">已结束</div>
          <div class="residue" v-else>{{ residueMin }} : {{ residueSec }}</div>
          <div>本次预约结束的倒计时</div>

          <div class="tittle">设备信息</div>
          <div class="word">设备名称：{{ deviceOperating.deviceName }}</div>
          <div class="word">设备标识：{{ deviceOperating.deviceId }}</div>
          <div class="word">设备类型：{{ device.type }}</div>
          <div v-if="device.type === 'MeasurementsLive'" class="word">设备在线状态：
            <span v-if="deviceOnline" style="color: #2DA2A6">在线</span>
            <span v-else style="color: brown">离线</span>
          </div>

          <div class="tittle" v-if="deviceOperating.liveUrl">操作视频</div>
          <!-- 以下是萤石直播方式播放 -->
          <!-- <video ref="liveVideo" class="video" controls muted autoplay /> -->
          <!-- 以下采用监控播放的方式 -->
          <div id="video-container" class="video" v-if="deviceOperating.liveUrl"></div>
        </div>

        <div style="margin-top: 30px; margin-left: 20px;">
          <el-button @click="ChangeDisplayIframeUrl1()">实验内容</el-button>
          <el-button v-if="device.type === 'MeasurementsLive'" @click="ChangeDisplayIframeUrl2()">仪器操作</el-button>
          <el-button @click="screen()">全屏</el-button>

        </div>

        <div style="margin-top: 30px; margin-left: 20px; margin-right: 20px;">
          <div class="tittle">遇到问题？</div>
          <div style="margin-top: 10px;">
            <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" placeholder="请输入内容"
              v-model="commentForm.content">
            </el-input>
          </div>
          <div style="margin-top: 10px; text-align: right;">
            <el-button @click="toComment()">给老师留言</el-button>
            <el-popover ref="popover5" placement="top" width="160" v-model="visible2">
              <p>这个操作将通过远程实验平台发送邮件给老师，确定继续吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="visible2 = false">取消</el-button>
                <el-button type="primary" size="mini" @click="sendEmail()">确定</el-button>
              </div>
            </el-popover>
            <el-button style="margin-left: 20px" v-popover:popover5>发邮件给老师</el-button>
          </div>
        </div>


      </el-aside>

      <el-aside width="40px" class="view-aside-off" v-if="!asideVisible">
        <i class="el-icon-s-unfold" @click="putAway()">
        </i>
      </el-aside>
      <el-main class="view-main">
        <iframe id="con_lf_top_div" v-if="displayIframeUrl" :src="displayIframeUrl" frameborder="0" class="view-iframe"
          allowfullscreen="true"></iframe>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// import flvjs from 'flv.js'
import EZUIKit from "ezuikit-js"
import appointmentService from '@/service/appointment'
import deviceService from '@/service/device'
import commentService from '@/service/comment'
var player = null;

export default {
  name: 'DeviceControl',
  data() {
    return {
      device: {
        type: undefined
      },
      deviceOperating: {
        deviceId: undefined,
        type: undefined,
        deviceName: undefined,
        signalServerUrl: undefined,
        signalServerApiKey: undefined,
        liveUrl: undefined,
        lessonId: undefined,
        lessonName: undefined,
        experimentOperationPageUrl: undefined
      },
      deviceOnline: false,
      asideVisible: true,
      lessonDetail: {
        lessonId: undefined,
        lessonName: undefined,
        experimentOperationPageUrl: undefined
      },
      appointmentFrom: {
        appointmentDate: undefined,
        startTime: undefined,
        endTime: undefined,
        deviceId: undefined
      },
      activeName: 'second',
      lessoniD: undefined,
      displayIframeUrl: undefined,
      fullscreen: false,
      commentForm: {
        content: undefined
      },
      visible2: false,
      iframeUrl: undefined,
      experimentIframeUrl: undefined,
      videoLoading: true,
      warned: false,
      residueMin: 0,
      residueSec: 0
    }
  },
  mounted() {
    this.appointmentFrom.appointmentDate = new Date(this.$route.params.date)
    this.appointmentFrom.startTime = this.$route.params.startTime
    this.appointmentFrom.endTime = this.$route.params.endTime
    this.appointmentFrom.deviceId = parseInt(this.$route.params.deviceId)
    // this.appointmentFrom.deviceType = parseInt(this.$route.params.type)
    this.appointmentFrom.lessonScheduleId = parseInt(this.$route.params.lessonScheduleId)
    this.lessonId = parseInt(this.$route.params.lessonId)
    this.fetchData()

  },
  methods: {
    goback() {
      this.$router.go(-1)
    },
    fetchData() {
      this.getDeviceOperating()
    },
    async getDeviceOperating() {
      const params = Object.assign({}, this.appointmentFrom)
      try {
        const { model } = await appointmentService.getDeviceOperating(params)
        this.deviceOperating = model
        this.device = (await deviceService.get(this.appointmentFrom.deviceId)).model
        this.iframeUrl = `https://measurementslive.ni.com/measure.html?id=${this.deviceOperating.deviceId}&signaling-server-address=${this.deviceOperating.signalServerUrl}`
        if (this.deviceOperating.experimentOperationPageUrl) {
          if (this.device.type === 'MeasurementsLive') {
            this.experimentIframeUrl = `${this.deviceOperating.experimentOperationPageUrl}?id=${this.deviceOperating.deviceId}`
          }
          else {
            this.experimentIframeUrl = `${this.deviceOperating.experimentOperationPageUrl}:${this.deviceOperating.deviceId}/vnc.html`
          }
        }
        this.displayIframeUrl = this.experimentIframeUrl

        player = new EZUIKit.EZUIKitPlayer({
          id: 'video-container', // 视频容器ID
          accessToken: this.deviceOperating.ysAccessToken,
          url: this.deviceOperating.liveUrl,
          template: '92d46ddc8f45417ab373131145645794',
          width: 450,
          height: 300,
        });
        window.player = player;
        this.countDown()
        this.websocket()
      } catch (e) {
        console.error(e.message)
        this.$message.error(e.message)
      }
    },
    websocket() {
      const apiKey = this.deviceOperating.signalServerApiKey
      const that = this

      const signalingServer = new WebSocket(`${this.deviceOperating.signalServerUrl}/api`)
      signalingServer.onopen = () => {
        signalingServer.send(JSON.stringify({
          apikey: apiKey,
          command: 'register'
        }))
      }
      signalingServer.onmessage = e => {
        const result = JSON.parse(e.data)
        if (result.type === 'register' && result.register === 'success') {
          setInterval(() => {
            signalingServer.send(JSON.stringify({
              apikey: apiKey,
              command: 'devices'
            }))
          }, 2000)
        }
        if (result.type === 'devices' && result.devices && result.devices.length > 0) {
          const device = result.devices.find(o => o.id === that.deviceOperating.deviceId)
          that.deviceOnline = !!device
        }
      }
    },
    ChangeDisplayIframeUrl1() {
      //实验内容  默认页面
      this.displayIframeUrl = this.experimentIframeUrl
    },
    ChangeDisplayIframeUrl2() {
      //仪器操作
      this.displayIframeUrl = this.iframeUrl
    },
    screen() {
      let case1 = document.getElementById('con_lf_top_div')
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } else {
        if (case1.requestFullscreen) {
          case1.requestFullscreen()
        } else if (case1.webkitRequestFullScreen) {
          case1.webkitRequestFullScreen()
        } else if (case1.mozRequestFullScreen) {
          case1.mozRequestFullScreen()
        } else if (case1.msRequestFullscreen) {
          // IE11
          case1.msRequestFullscreen()
        }
      }
    },
    toComment() {
      this.$confirm('确认提交吗？', '提示').then(async () => {
        this.commentForm.lessonId = this.lessonId
        console.log(this.commentForm)
        const {
          code,
          message
        } = await commentService.add(this.commentForm)
        if (code === 0) {
          // await this.fetchData()
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
    sendEmail() {
      this.$confirm('确认发送吗？', '提示').then(async () => {
        this.commentForm.lessonId = this.lessonId
        this.commentForm.lessonScheduleID = this.appointmentFrom.lessonScheduleId
        console.log(this.commentForm)
        const { code, message } = await commentService.sendemail(this.commentForm)
        if (code === 0) {
          // await this.fetchData()
          this.commentForm.content = ''
          this.$message.success('操作成功')
        } else {
          this.$message.error(message)
          this.$message.error(message2)
        }
      }).catch(e => {
        console.error(e)
        this.$message.info('操作取消')
      })
    },
    countDown() {
      const end = new Date(this.$route.params.date + ' ' + this.$route.params.endTime)

      const ms = end.getTime() - new Date().getTime()
      const min = ms / 1000 / 60
      const sec = ms / 1000 % 60
      this.residueMin = parseInt(min.toString()) < 0 ? 0 : parseInt(min.toString())
      this.residueSec = parseInt(sec.toString()) < 0 ? 0 : parseInt(sec.toString())

      const p = setInterval(() => {
        const ms = end.getTime() - new Date().getTime()
        const min = ms / 1000 / 60
        const sec = ms / 1000 % 60
        this.residueMin = parseInt(min.toString()) < 0 ? 0 : parseInt(min.toString())
        this.residueSec = parseInt(sec.toString()) < 0 ? 0 : parseInt(sec.toString())

        if (this.residueMin === 0 && !this.warned) {
          this.$message.warning({
            message: '本次操作还剩1分钟',
            showClose: true,
            duration: 6000
          })
          this.warned = true
        }

        if (this.residueMin === 0 && this.residueSec === 0) {
          this.deviceOperating.liveUrl = ''
          this.iframeUrl = ''
          this.experimentIframeUrl = ''
          this.displayIframeUrl = null
          // this.$router.go(0)
          player.stop()
          // clearInterval(p)
        }
      }, 1000)
    },
    putAway() {
      this.asideVisible = !this.asideVisible
    },
    handleClick(tab, event) {
    }
  }
}
</script>
<style lang="scss" scoped>
.view {
  margin-top: 60px;
  z-index: -1;
  overflow: hidden;

  .view-aside {
    height: calc(100vh - 60px);
    background-color: #fff;

    .aside-tittle {
      margin-top: 20px;
      margin-left: 20px;
      margin-bottom: 40px;
      margin-right: 20px;
      width: 440px;
      display: flex;
      justify-content: flex-start;

      .lessonDetailName {
        font-size: 16px;
        font-weight: 600;
        color: rgba(0, 0, 0, 0.85);
        line-height: 24px;
      }

      .el-icon-s-fold {
        font-size: 24px;
        color: #2DA2A6;
      }
    }

    .view-instruction {
      margin-left: 20px;
      margin-right: 20px;
      font-size: 14px;
      line-height: 24px;
      color: rgba(0, 0, 0, 0.85);

      .residue {
        color: #FFA940;
        font-weight: 700;
        font-size: 16px;
      }

      .over {
        color: #FF4D4F;
        font-weight: 700;
        font-size: 16px;
        margin-top: 12px;
      }

      .tittle {
        font-weight: 700;
        font-size: 16px;
        margin-top: 40px;
      }

      .word {
        margin-top: 8px;
      }

      .video {
        margin-top: 40px;
        width: 100%;
        height: 100%;
        border: 1px black solid;
      }
    }
  }

  .view-aside-off {
    height: calc(100vh - 60px);
    background-color: #fff;
    text-align: center;
    font-size: 24px;

    .el-icon-s-unfold {
      color: #2DA2A6;
      margin-top: 20px;
      text-align: center;
    }
  }

  .view-main {
    margin-right: 20px;
    margin-left: 20px;
    margin-top: 20px;
    padding: 0;
    // overflow: hidden;
    // height: 100%;
    // background-color: #fff;

    ::v-deep .el-tabs__nav {
      margin-left: 20px;
      margin-top: 8px;
    }

    ::v-deep .el-tabs__item {
      font-size: 16px;
      margin-bottom: 6px;
    }

    .view-iframe {
      // margin 调整后需要调整这里的 30px
      // height: calc(100vh + 30px - 80px);
      height: 98%;
      width: 100%;
      // 部分隐藏
      // margin-top: -50px;
      // 全部隐藏
      // margin-top: -88px;
    }

    .view-iframe-nxg {
      // margin 调整后需要调整这里的 30px
      height: calc(100vh + 30px - 80px);
      width: 100%;
    }
  }
}
</style>
