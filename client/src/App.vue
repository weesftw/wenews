<template>
  <div class="d-flex flex-column min-vh-100">
    <NavBar />
    <router-view />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App'
})
</script>
