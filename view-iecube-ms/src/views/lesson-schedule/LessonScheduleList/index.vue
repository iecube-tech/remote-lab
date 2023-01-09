<template>
  <div>
    <el-tabs v-model="activeName" type="border-card" @tab-click="tabChange">
      <el-tab-pane
        v-for="item in tabs"
        :key="'teacherLessonScheduleTab' + item.name"
        :label="item.label"
        :name="item.name"
      >
        <lesson-schedule-list-tab :ref="item.name" :type="item.name" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>

import LessonScheduleListTab from '@/views/lesson-schedule/LessonScheduleListTab'

export default {
  name: 'LessonSchedule',
  components: { LessonScheduleListTab },
  data() {
    return {
      activeName: 'CURRENT',
      tabs: [
        {
          name: 'CURRENT',
          label: '当前排课'
        },
        {
          name: 'END',
          label: '已结束'
        }
      ]
    }
  },
  methods: {
    tabChange() {
      this.$refs[this.activeName][0].fetchData()
    }
  }
}
</script>
<style scoped>

</style>
