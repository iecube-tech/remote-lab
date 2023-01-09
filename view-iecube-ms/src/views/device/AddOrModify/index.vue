<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" label-width="100px">
      <el-form-item label="设备类型">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option
            v-for="item in typeDevice.enum"
            :key="'user' + item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备ID">
        <el-input v-model="form.deviceId" />
      </el-form-item>
      <el-form-item label="设备名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="设备位置">
        <el-input v-model="form.location" />
      </el-form-item>
      <el-form-item label="直播链接">
        <el-input v-model="form.liveUrl" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择">
          <el-option
            v-for="item in enableStatusEnum.enum"
            :key="'user' + item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="courseList && courseList.length" label="绑定课程">
        <el-checkbox-group v-model="form.courseIdList">
          <el-row :gutter="8">
            <el-col
              v-for="item in courseList"
              :key="'course' + item.id"
              :span="8"
              style="margin-bottom: 4px"
            >
              <el-checkbox :label="item.id">{{ item.name }}</el-checkbox>
            </el-col>
          </el-row>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirm">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import enableStatusEnum from '@/enums/enable-status-enum'
import typeDevice from '@/enums/type-device'
import deviceService from '@/service/device'
import teacherCourseService from '@/service/teacher-course'

export default {
  name: 'DeviceAddOrModify',
  props: {
    id: {
      type: Number,
      default: undefined
    },
    type: {
      type: String,
      default: 'ADD'
    }
  },
  data() {
    return {
      enableStatusEnum,
      typeDevice,
      courseList: [],
      form: {
        id: undefined,
        deviceId: undefined,
        type: 'MeasurementsLive',
        name: undefined,
        signalServerUrl: undefined,
        location: undefined,
        liveUrl: undefined,
        status: 'ENABLE',
        courseIdList: []
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listCourse()
      if (this.type === 'MODIFY' && this.id) {
        this.get()
      }
    },
    async listCourse() {
      const { model } = await teacherCourseService.listGrant({ pageMode: 0 })
      this.courseList = model.content
    },
    async get() {
      const { model } = await deviceService.get(this.id)
      model.courseIdList = model.courseIdList || []
      this.form = model
    },
    async confirm() {
      const params = Object.assign({}, this.form)
      let success
      try {
        if (this.type === 'ADD') {
          const { code } = await deviceService.add(this.form)
          success = code === 0
        } else if (this.type === 'MODIFY') {
          params.id = this.id
          const { code } = await deviceService.modify(params)
          success = code === 0
        }
      } catch (e) {
        console.error(e)
        success = false
      }
      if (success) {
        this.$message.success('操作成功')
        this.$emit('on-success')
      } else {
        this.$message.error('系统异常')
      }
    },
    cancel() {
      this.$emit('on-cancel')
    }
  }
}
</script>

<style scoped>

</style>
