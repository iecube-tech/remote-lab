<template>
  <div>
    <div class="course-container">
      <span v-for="course in courseList" :key="'course' + course.id" class="card" @click="jump(course.id)">
        <img class="card-img" :src="'local-resource/' + course.coverUrl" alt="course">
        <div class="card-tittle"> {{ course.name }}</div>
        <div class="card-introduction"> {{ course.summary }}</div>
      </span>
    </div>
  </div>
</template>

<script>
import schoolCourseService from '../service/school-course'
import lessonService from '@/service/lesson'

export default {
  name: 'PublicCourse',
  data () {
    return {
      courseList: []
    }
  },
  mounted () {
    this.fetchData()
  },
  methods: {
    async jump (id) {
      const { model } = await lessonService.listByCourseId(id)
      await this.$router.push({
        name: 'LessonDetail',
        params: {
          type: 'public',
          courseId: id,
          lessonId: model[0].id
        }
      })
    },
    fetchData () {
      this.list()
    },
    async list () {
      const { model } = await schoolCourseService.list({ pageMode: 0 })
      this.courseList = model.content
    }
  }
}
</script>

<style lang="scss" scoped>
$main: #2DA2A6;

.course-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 80px;

  .card {
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

  .card:hover {
    box-shadow: 0px 4px 32px 0px rgba(0, 0, 0, 0.25);
  }

  .card-img {
    width: 400px;
    height: 240px;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
  }

  .card-tittle {
    font-size: 20px;
    font-weight: 600;
    color: $main;
    line-height: 30px;
    margin: 20px;
  }

  .card-introduction {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.65);
    line-height: 24px;
    margin: 0 20px;
  }
}
</style>
