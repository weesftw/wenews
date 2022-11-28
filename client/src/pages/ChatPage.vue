<template>
  <q-page class="fit">
    <router-view :key="$route.fullPath">
      <SocketComponent />
    </router-view>
  </q-page>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import SocketComponent from 'components/SocketComponent.vue'

export default defineComponent({
  name: 'ChatPage',
  components: {SocketComponent}
});
</script>
