<template>
  <div class="view">
    <el-container>
      <el-aside width="480px" class="view-aside" v-if="asideVisible">

        <div class="aside-tittle">
          <span class="lessonDetailName">{{ deviceOperating.lessonName }}</span>
          <i class="el-icon-s-fold" style="cursor: pointer" @click="putAway()"></i>
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

      </el-aside>

      <el-aside width="40px" class="view-aside-off" v-if="!asideVisible">
        <i class="el-icon-s-unfold" @click="putAway()">
        </i>
      </el-aside>

      <el-main class="view-main">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane v-if="device.type === 'MeasurementsLive'" label="仪器操作" name="first">
            <iframe v-if="iframeUrl" :src="iframeUrl" frameborder="0" class="view-iframe"></iframe>
          </el-tab-pane>
          <el-tab-pane label="实验内容" name="second">
            <iframe v-if="experimentIframeUrl" :src="experimentIframeUrl" frameborder="0"
              class="view-iframe-nxg"></iframe>
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// import flvjs from 'flv.js'
import EZUIKit from "ezuikit-js"
import appointmentService from '@/service/appointment'
import deviceService from '@/service/device'
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
    this.fetchData()

  },
  methods: {
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
        this.countDown()
        this.websocket()
        // if (flvjs.isSupported()) {
        //   const videoElement = this.$refs.liveVideo
        //   const flvPlayer = flvjs.createPlayer({
        //     type: 'flv',
        //     url: this.deviceOperating.liveUrl
        //   })
        //   flvPlayer.attachMediaElement(videoElement)
        //   flvPlayer.load()
        //   flvPlayer.play()
        //   this.videoLoading = false
        // } else {
        //   this.$message.warning('您的浏览器暂不支持此类直播链接')
        // }
        // 以上为萤石直播播放方式，以下切换萤石监控播放方式。监控播放方式延迟较小
        console.log("111111111111111")
        console.log(this.deviceOperating.liveUrl)

        player = new EZUIKit.EZUIKitPlayer({
          id: 'video-container', // 视频容器ID
          accessToken: 'at.23qmf3425erq3hiy1s9fz9100a5sf0e4-45gybzlgyn-1n6ytc8-3fc4lrdbe',
          url: 'ezopen://open.ys7.com/K81970910/1.hd.live',
          // simple - 极简版; pcLive-pc直播；pcRec-pc回放；mobileLive-移动端直播；mobileRec-移动端回放;security - 安防版;voice-语音版;
          // 使用自定义模板
          template: '92d46ddc8f45417ab373131145645794',
          // plugin: ['talk'], // 加载插件，talk-对讲
          width: 450,
          height: 300,
        });
        window.player = player;

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
          player.stop()
          player.destroy()
          this.deviceOperating.liveUrl = ''
          this.iframeUrl = ''
          this.experimentIframeUrl = ''
          clearInterval(p)
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
      justify-content: space-between;

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
    overflow: hidden;
    background-color: #fff;

    /deep/ .el-tabs__nav {
      margin-left: 20px;
      margin-top: 8px;
    }

    /deep/ .el-tabs__item {
      font-size: 16px;
      margin-bottom: 6px;
    }

    .view-iframe {
      // margin 调整后需要调整这里的 30px
      height: calc(100vh + 30px - 80px);
      width: 100%;
      // 部分隐藏
      margin-top: -48px;
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
