<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="border-card" @tab-click="tabChange">
      <el-tab-pane
        v-for="item in tabs"
        :key="'homeworkTab' + item.name"
        :label="item.label"
        :name="item.name"
      >

      </el-tab-pane>
      <router-view />
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'HomeworkList',
  data() {
    return {
      activeName: 'LIST',
      tabs: [
        {
          name: 'LIST',
          label: '作业管理',
          routerName: 'HomeworkList'
        },
        {
          name: 'STATISTICS',
          label: '作业统计',
          routerName: 'HomeworkStatistics'
        },
        {
          name: 'DETAIL',
          label: '课程作业统计',
          routerName: 'HomeworkCourseDetail'
        }
      ],
      tabRouter: {
        'LIST': 0,
        'STATISTICS': 1,
        'DETAIL': 2
      },
      routerTab: {
        'HomeworkList': 0,
        'HomeworkStatistics': 1,
        'HomeworkCourseDetail': 2
      },
      loading: false
    }
  },
  mounted() {
    const activeName = this.tabs[this.routerTab[this.$route.name]].name
    if (activeName) {
      this.activeName = activeName
    }
  },
  methods: {
    tabChange() {
      this.$router.push({
        name: this.tabs[this.tabRouter[this.activeName]].routerName
      })
    },
    rating(type, id) {
      this.showAddOrModifyDialog = true
    }
  }
}
</script>
<style scoped>
.page-wrapper {
  margin-top: 12px;
  text-align: center;
}

.card-title {
  width: 96px;
  height: 36px;
  font-size: 24px;
  font-weight: 600;
  line-height: 36px;
}

.list-button {
  float: right;
  padding: 3px 0;
  margin-left: 16px
}
</style>
