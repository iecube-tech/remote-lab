<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" label-width="100px">
      <el-form-item label="名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="学号/工号">
        <el-input v-model="form.num" />
      </el-form-item>
      <el-form-item label="学院">
        <el-input v-model="form.faculty" />
      </el-form-item>
      <el-form-item label="年级">
        <el-input v-model="form.grade" />
      </el-form-item>
      <el-form-item label="班级">
        <el-input v-model="form.gradeClass	" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email	" />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option value="TEACHER" label="老师" />
          <el-option value="STUDENT" label="学生" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.schoolStatus" placeholder="请选择">
          <el-option
            v-for="item in schoolUserStatusEnum.enum"
            :key="'user' + item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirm">创建</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import schoolUserStatusEnum from '@/enums/user-school-status-enum'
import userService from '@/service/user'

export default {
  name: 'SchoolAddOrModify',
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
      schoolUserStatusEnum: schoolUserStatusEnum,
      form: {
        name: undefined,
        num: undefined,
        faculty: undefined,
        grade: undefined,
        gradeClass: undefined,
        email: undefined,
        schoolStatus: 'IN_SCHOOL'
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      if (this.type === 'MODIFY' && this.id) {
        this.get()
      }
    },
    async get() {
      const { model } = await userService.get(this.id)
      this.form = model
    },
    async confirm() {
      const params = Object.assign({}, this.form)
      try {
        if (this.type === 'ADD') {
          const { code } = await userService.add(params)
          if (code === 0) {
            this.$emit('on-success')
          }
        } else if (this.type === 'MODIFY') {
          params.id = this.id
          const { code } = await userService.modify(params)
          if (code === 0) {
            this.$emit('on-success')
          }
        }
      } catch (e) {
        console.error(e)
        this.$message.error(e.message)
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
