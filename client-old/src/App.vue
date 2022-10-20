<template>
  <div id="app" class="d-flex flex-column min-vh-100">
    <NavBar :key="$route.fullPath" />
    <div class="container pb-5">
      <router-view />
    </div>
    <FooterBar />
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'
import FooterBar from './components/FooterBar.vue'

export default {
  name: 'App',
  components: {
    NavBar,
    FooterBar
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
}
</style>
