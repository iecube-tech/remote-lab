<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <EZU />
  </div>
</template>

<script>
// @ is an alias to /src
// import HelloWorld from '@/components/HelloWorld.vue'
import EZU from '@/components/ezuikit.vue'

export default {
  name: 'HomeView',
  components: {
    EZU
  }
}
</script>
