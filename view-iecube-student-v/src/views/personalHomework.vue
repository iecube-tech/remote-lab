<template>
  <div>
    <div class="lesson-container">
      <span v-for="lesson in lessonArray" :key="'lesson' + lesson.lessonId" class="card" @click="jump(lesson)">
        <img class="card-img" :src="'local-resource/' + lesson.coverUrl" alt="lesson">
        <div class="word-container">
          <div class="card-tittle"> {{ lesson.lessonName }}</div>
          <div class="card-homework">课节作业 {{ lesson.resource.filename }}</div>
          <div v-if="lesson.score !== ''" class="card-number">{{ lesson.score }}
            <span class="card-number-word">分</span>
          </div>
        </div>
      </span>
    </div>
  </div>
</template>

<script>

import homeworkService from '@/service/homework'

export default {
  name: 'PersonalHomework',
  data () {
    return {
      lessonArray: []
    }
  },
  mounted () {
    this.fetchData()
  },
  methods: {
    async jump (lesson) {
      await this.$router.push({
        name: 'LessonScheduleDetail',
        params: {
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
      const { model } = await homeworkService.list({})
      this.lessonArray = model.content
    }
  }
}
</script>

<style lang="scss" scoped>
$main: #2DA2A6;

.lesson-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 80px;

  .card {
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

  .card:hover {
    box-shadow: 0px 4px 32px 0px rgba(0, 0, 0, 0.25);
  }

  .card-img {
    width: 300px;
    height: 240px;
    display: inline-block;
  }

  .word-container {
    display: inline-block;
    height: 200px;
  }

  .card-tittle {
    font-size: 20px;
    font-weight: 600;
    color: $main;
    line-height: 30px;
    margin: 20px;
  }

  .card-homework {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.65);
    line-height: 24px;
    margin: 8px 20px;
  }

  .card-number {
    font-size: 48px;
    font-weight: 600;
    color: #FFA940;
    line-height: 72px;
    margin: 40px 0 0 20px;
  }

  .card-number-word {
    font-size: 16px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
    line-height: 24px;
    margin-left: -8px;
  }
}
</style>
