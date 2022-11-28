<template>
  <q-page class="row items-center justify-evenly">
    <router-view :key="$route.fullPath">
      <NewsDashboardComponent />
    </router-view>
  </q-page>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import NewsDashboardComponent from 'components/dashboard/NewsDashboardComponent.vue';

export default defineComponent({
  name: 'NewsDashboardPage',
  components: {NewsDashboardComponent}
});
</script>
