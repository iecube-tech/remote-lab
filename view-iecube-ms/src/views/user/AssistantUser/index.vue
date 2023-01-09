<template>
  <div>
    <user-common-list view-type="ASSISTANT" />
  </div>
</template>

<script>

import UserCommonList from '@/views/user/UserList'

export default {
  name: 'AssistantUserList',
  components: { UserCommonList }
}
</script>
<style scoped>
</style>
