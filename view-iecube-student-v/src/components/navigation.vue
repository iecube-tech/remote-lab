<template>
  <div>
    <div class="navigation">
      <div class="logoAndTittle" @click="jump('publicCourse')">
        <img v-if="schoolLogoUrl" style="display: inline-block; margin-left: 20px; height: 48px;" :src="'local-resource/' + schoolLogoUrl" alt="logo">
        <img v-if="!schoolLogoUrl" class="logo" src="@/assets/images/logo.svg" alt="logo">
        <span v-if="!schoolLogoUrl" class="tittle">IECube</span>
      </div>
      <el-tabs v-model="activeIndex" class="tabs" @tab-click="handleClick">
        <el-tab-pane :key="item.name"
                     v-for="item in routes"
                     :label="item.meta.title"
                     :name="item.name"
        />

        <!--        <el-tab-pane label="公开课程" name="0">公开课程</el-tab-pane>
                <el-tab-pane label="我的课程" name="1">我的课程</el-tab-pane>
                <el-tab-pane label="课程收藏" name="2">课程收藏</el-tab-pane>
                <el-tab-pane label="我的预约" name="3">我的预约</el-tab-pane>
                <el-tab-pane label="我的作业" name="4">我的作业</el-tab-pane>-->
      </el-tabs>

      <div class="item">
        <span class="name">{{ name }}</span>
        <el-tooltip effect="dark" content="个人中心" placement="bottom-end">
          <img class="portrait" src="@/assets/images/portrait.svg" alt="个人中心" @click="jump('personalInfo')">
        </el-tooltip>
      </div>

    </div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'Navigation',
  data () {
    return {
      activeIndex: undefined,
      // activeIndex: '',
      navigationArry: ['publicCourse', 'personalCourse', 'courseCollection', 'personalAppointment', 'personalHomework'],
      navigationRouterArry: ['public_course', 'personal_course', 'course_collection', 'personal_appointment', 'personal_homework']
    }
  },
  computed: {
    routes () {
      return this.$router.options.routes[1].children.filter(o => !o.hidden)
    },
    route () {
      return this.$store.state.app.route
    },
    schoolLogoUrl () {
      return this.$store.getters.schoolLogoUrl
    },
    name () {
      return this.$store.getters.name
    }
  },
  created: function () {

  },
  mounted: function () {
    const that = this
    const activeNav = (that, route) => {
      if (route.meta && route.meta.active) {
        that.activeIndex = route.meta.active
      } else {
        that.activeIndex = route.name
      }
      /*if (route.name === 'courseDetail') {
        if (that.$route.params.type === 'public') {
          that.activeIndex = 'publicCourse'
        } else if (that.$route.params.type === 'schedule') {
          that.activeIndex = 'personalCourse'
        }
      } else {
        if (route.meta.activeNav) {
          that.activeIndex = route.meta.activeNav
        } else if (!route.hidden) {
          that.activeIndex = route.name
        }
      }*/
    }
    this.$bus.$on('on-route-change', (route) => {
      activeNav(that, route)
    })
    activeNav(this, this.$route)
  },
  methods: {
    jump: function (router) {
      this.$router.push({ name: router })
    },
    handleClick () {
      // console.log(this.activeIndex, this.navigationArry[this.activeIndex])
      // this.$router.push({ name: this.navigationArry[this.activeIndex] })
      this.$router.push({ name: this.activeIndex })
    }
  }
}
</script>

<style lang="scss" scoped>
$main: #2DA2A6;

.navigation {
  width: 100%;
  height: 60px;
  background: #FFFFFF;
  box-shadow: 0px 2px 12px 0px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: fixed;
  z-index: 1;
  top: 0;

  .logoAndTittle {
    display: flex;
    align-items: center;
    cursor: pointer;

    .logo {
      display: inline-block;
      margin-left: 20px;
      width: 36px;
      height: 36px;
    }

    .tittle {
      font-size: 20px;
      font-weight: 600;
      color: $main;
      line-height: 30px;
      margin-left: 10px;
    }
  }

  .tabs {
    margin-top: 10px;

    /deep/ .el-tabs__content {
      display: none;
    }

    /deep/ .el-tabs__nav-wrap::after {
      background-color: #FFFFFF;
    }

    /deep/ .el-tabs__item {
      opacity: 0.65;
    }

    /deep/ .is-active {
      opacity: 1;
    }
  }

  .item{
    display: flex;
    align-items: center;

    .name{
      color: rgba(0, 0, 0, 0.65);
      line-height: 24px;
      margin-right: 12px;
      font-size: 14px;
    }

    .portrait {
      display: inline-block;
      margin-right: 20px;
      cursor: pointer;
    }
  }
}
</style>
