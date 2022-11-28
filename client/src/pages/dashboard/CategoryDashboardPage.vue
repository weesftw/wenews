<template>
  <q-page class="row items-center justify-evenly">
    <router-view :key="$route.fullPath">
      <CategoryDashboardComponent />
    </router-view>
  </q-page>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import CategoryDashboardComponent from 'components/dashboard/CategoryDashboardComponent.vue';

export default defineComponent({
  name: 'CategoryDashboardPage',
  components: {CategoryDashboardComponent}
});
</script>
