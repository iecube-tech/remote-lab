<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="选择设备" prop="id">
        <el-select v-model="form.id">
          <el-option
            v-for="item in deviceList"
            :key="'lessonScheduleAddDevice' + item.id"
            :value="item.id"
            :label="item.name"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <div>
      <el-button type="primary" @click="confirm">确认</el-button>
      <el-button @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import deviceService from '@/service/device'

export default {
  name: 'LessonScheduleAddDevice',
  props: {
    courseId: {
      type: Number,
      default: undefined
    }
  },
  data() {
    return {
      deviceList: [],
      deviceMap: {},
      form: {
        id: undefined
      },
      rules: {
        id: [
          { required: true, message: '必填项' }
        ]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listDevice()
    },
    async listDevice() {
      const params = {
        pageMode: 0,
        courseId: this.courseId
      }
      const { model } = await deviceService.list(params)
      this.deviceList = model.content
      for (let i = 0; i < this.deviceList.length; i++) {
        this.deviceMap[this.deviceList[i].id] = this.deviceList[i]
      }
    },
    cancel() {
      this.$emit('on-cancel')
    },
    confirm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const device = this.deviceMap[this.form.id]
          this.form.id = device.id
          this.form.deviceId = device.deviceId
          this.form.location = device.location
          this.form.deviceName = device.name
          this.form.devicetype = device.type
          this.$emit('on-confirm', this.form)
        }
      })
    }
  }
}
</script>

<style scoped>
.device-explain-img-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.device-explain-img-upload:hover {
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
