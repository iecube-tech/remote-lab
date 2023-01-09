<template>
  <div>
    <div class="course-container">
      <span v-for="course in courseArray" :key="'course' + course.courseId" class="card" @click="jump(course.courseId)">
        <img class="card-img" :src="'local-resource/' + course.coverUrl" alt="course">
        <div class="card-tittle"> {{ course.courseName }}</div>
        <div class="card-introduction"> {{ course.summary }}</div>
      </span>
    </div>
  </div>
</template>

<script>
import studentCourseService from '@/service/student-course'

export default {
  name: 'PublicCourse',
  data () {
    return {
      courseArray: []
    }
  },
  mounted () {
    this.fetchData()
  },
  methods: {
    async jump (courseId) {
      const { model } = await studentCourseService.listLessonSchedule(courseId)
      let lesson
      for (let i = 0; i < model.length; i++) {
        if (model[i].lessonScheduleId) {
          lesson = model[i]
          break
        }
      }
      await this.$router.push({
        name: 'LessonScheduleDetail',
        params: {
          type: 'schedule',
          courseId: lesson.courseId,
          lessonId: lesson.lessonId,
          lessonScheduleId: lesson.lessonScheduleId
        }
      })
    },
    fetchData () {
      this.list()
    },
    async list () {
      const { model } = await studentCourseService.list({ pageMode: 0 })
      this.courseArray = model.content
    }
  }
}
</script>

<style lang="scss" scoped>
$main: #2DA2A6;

.course-container{
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 80px;

  .card{
    width: 400px;
    height: 400px;
    background: #FFFFFF;
    box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.1);
    border-radius: 20px;
    display: inline-block;
    margin: 20px;
    cursor: pointer;
    overflow: hidden;
  }

  .card:hover{
    box-shadow: 0px 4px 32px 0px rgba(0, 0, 0, 0.25);
  }

  .card-img{
    width: 400px;
    height: 240px;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
  }

  .card-tittle{
    font-size: 20px;
    font-weight: 600;
    color: $main;
    line-height: 30px;
    margin: 20px ;
  }

  .card-introduction{
    font-size: 14px;
    color: rgba(0, 0, 0, 0.65);
    line-height: 24px;
    margin: 0 20px;
  }
}
</style>
