<template>
  <div>
    <div class="lesson-container">
      <span v-for="lesson in appointmentArray" :key="'lesson' + lesson.lessonId + lesson.appointmentDate + lesson.startTime + lesson.endTime + lesson.deviceId" class="card" @click="jump(lesson)">
        <img class="card-img" :src="'local-resource/' + lesson.lessonCoverUrl" alt="lesson">
        <div class="word-container">
          <div class="card-tittle"> {{ lesson.lessonName }}</div>
          <div class="card-date">预约日期: {{ lesson.appointmentDate }}</div>
          <div class="card-time">预约时间: {{ lesson.startTime }}-{{ lesson.endTime }}</div>
          <div class="card-deviceId">预约设备: {{ lesson.deviceName }}</div>
          <div v-if="lesson.state" class="card-state">已过期</div>
        </div>
      </span>
    </div>
  </div>
</template>

<script>
import appointmentService from '@/service/appointment'
import lessonService from '@/service/lesson'
export default {
  name: 'PersonalAppointment',
  data () {
    return {
      appointmentArray: []
    }
  },
  mounted () {
    this.fetchData()
  },
  methods: {
    async jump (lesson) {
      const { model } = await lessonService.get(lesson.lessonId)
      await this.$router.push({
        name: 'LessonScheduleDetail',
        params: {
          type: 'schedule',
          courseId: model.courseId,
          lessonId: lesson.lessonId,
          lessonScheduleId: lesson.lessonScheduleId
        }
      })
    },
    fetchData () {
      this.list()
    },
    async list () {
      const { model } = await appointmentService.list({})
      this.appointmentArray = model.content
    }
  }
}
</script>

<style lang="scss" scoped>
$main: #2DA2A6;

.lesson-container{
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 80px;

  .card{
    width: 640px;
    height: 240px;
    background: #FFFFFF;
    box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.1);
    margin: 20px;
    display: flex;
    align-items: flex-start;
    cursor: pointer;
    overflow: hidden;
  }

  .card:hover{
    box-shadow: 0px 4px 32px 0px rgba(0, 0, 0, 0.25);
  }

  .card-img{
    width: 300px;
    height: 240px;
    display: inline-block;
  }

  .word-container{
    display: inline-block;
    height: 200px;
  }

  .card-tittle{
    font-size: 20px;
    font-weight: 600;
    color: $main;
    line-height: 30px;
    margin: 20px ;
  }

  .card-date, .card-time, .card-deviceId{
    font-size: 14px;
    color: rgba(0, 0, 0, 0.65);
    line-height: 24px;
    margin: 8px 20px;
  }

  .card-state{
    font-size: 20px;
    font-weight: 600;
    color: #FFA940;
    line-height: 24px;
    margin: 20px 0 0 20px;
  }
}
</style>
