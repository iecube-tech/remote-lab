<template>
  <div id="app">
    <router-view />
    <div v-if="icp" class="icp" title="https://beian.miit.gov.cn/" @click="open">
      京ICP备20018557号-2
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      icp: true
    }
  },
  watch: {
    '$route'(to, from) {
      this.showIcp()
    }
  },
  mounted() {
    this.showIcp()
  },
  methods: {
    showIcp() {
      this.icp = this.$route.name === 'Login' || this.$route.path === '/login'
    },
    open() {
      window.open('https://beian.miit.gov.cn/')
    }
  }
}
</script>

<style lang="scss">
.icp {
  width: 100%;
  text-align: center;
  bottom: 50px;
  font-size: 14px;
  position: absolute;
  font-weight: 400;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.7);
}
</style>
