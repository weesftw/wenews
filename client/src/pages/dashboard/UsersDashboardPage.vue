<template>
  <q-page class="fit">
    <router-view :key="$route.fullPath">
      <UsersDashboardComponent />
    </router-view>
  </q-page>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import UsersDashboardComponent from 'components/dashboard/UsersDashboardComponent.vue';

export default defineComponent({
  name: 'UsersDashboardPage',
  components: {UsersDashboardComponent}
});
</script>
