<template>
  <div>
    <el-form ref="form" style="width: 80%" :model="form" label-width="100px">
      <el-form-item label="工号">
        <el-input v-model="form.num" />
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择">
          <el-option
            v-for="item in enableStatusEnum.enum"
            :key="'status' + item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirm">确定</el-button>
        <el-button @click="closeDialog">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import enableStatusEnum from '@/enums/enable-status-enum'
import userService from '@/service/user'

export default {
  name: 'OrgUserAddOrModify',
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
      form: {
        num: undefined,
        name: undefined,
        email: undefined,
        status: undefined,
        type: 'USER_ORG'
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
    confirm() {
      const params = Object.assign({}, this.form)
      if (this.type === 'ADD') {
        userService.add(params).then(res => {
          this.$emit('on-success')
          this.$message.success('操作成功')
        }).catch(e => {
          console.error(e)
          this.$message.error('系统异常')
        })
      } else if (this.type === 'MODIFY') {
        params.id = this.id
        userService.modify(params).then(res => {
          this.$emit('on-success')
        }).catch(e => {
          console.error(e)
          this.$message.error(e.message)
        })
      }
    },
    // 取消点击 关闭弹层
    closeDialog() {
      this.$emit('on-cancel')
    }
  }
}
</script>

<style scoped>

</style>
