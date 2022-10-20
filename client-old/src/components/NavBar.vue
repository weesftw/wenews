<template>
  <nav class="navbar navbar-expand-sm navbar-dark bg-dark">
    <div class="container">
      <a class="navbar-brand" href="/">{{ projectName }}</a>
      <div>
        <router-link v-if="!isSignedIn" class="btn btn-danger" to="/signIn">Sign In</router-link>
        <div v-if="isSignedIn">
          <div class="dropdown">
            <button class="btn btn-danger dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
              Welcome, {{ username }}
            </button>
            <div class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton1">
              <div v-if="isAdmin">
                <router-link class="dropdown-item" to="/dashboard">Dashboard</router-link>
              </div>
              <li>
                <hr class="dropdown-divider">
              </li>
              <a class="dropdown-item" href="#" @click="signOut">Logout</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import UserService from "@/service/UserService";
import router from "@/router";

export default {
  name: "NavBar",

  data() {
    return {
      isSignedIn: UserService.isSignedIn(),
      isAdmin: UserService.isAdmin(),
      projectName: process.env.VUE_APP_TITLE,
      username: localStorage.getItem('username')
    }
  },

  methods: {
    signOut() {
      UserService.signOut();
      router.push('/login')
    }
  }
}
</script>

<style scoped>

</style>