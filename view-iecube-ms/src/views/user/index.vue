<template>
  <div class="app-container">
    <user-common-list view-type="IN_SCHOOL" />
  </div>
</template>

<script>
import UserCommonList from '@/views/user/UserList'

export default {
  name: 'UserList',
  components: { UserCommonList }
}
</script>
<style scoped>
</style>
