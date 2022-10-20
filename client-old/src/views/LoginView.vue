<template>
  <HeaderBar/>
  <main>
    <div class="row">
      <div class="col">
        <div class="card shadow">
          <div class="card-header text-center fw-bolder">
            Sign in
          </div>
          <div class="card-body">
            <div class="d-flex justify-content-center">
              <div class="row g-1 col-lg-5">
                <div v-if="hasError" class="alert alert-danger" role="alert">
                  Wrong username or password.
                </div>
                <div class="mb-3">
                  <label for="username" class="form-label">Username</label>
                  <input v-model="username" name="username" id="username" type="text" class="form-control">
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">Password</label>
                  <input v-model="password" name="password" id="password" type="password" class="form-control">
                  <div id="emailHelp" class="form-text">We'll never share your password with anyone else.</div>
                </div>
                <div class="text-end">
                  <button @click="login" type="submit" class="btn btn-danger">Sign In</button>
                </div>
                <div class="text-end">

                </div>
              </div>
            </div>
          </div>
          <div class="card-footer text-muted text-end">Don't you have an account? <router-link to="/register">Register Now</router-link>.</div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import HeaderBar from '../components/HeaderBar.vue'
import AuthService from '../service/UserService'
import router from "@/router";

export default {
  name: "LoginView",
  components: {
    HeaderBar
  },

  data() {
    return {
      hasError: false,
      username: '',
      password: ''
    }
  },

  methods: {
    login() {
      AuthService.signIn({username: this.username, password: this.password})
          .catch(() => {
            this.hasError = true;
            this.password = ''
          })
          .then(value => {
            if(value != null)
              if(value.status === 200)
                localStorage.setItem('token', value.data.access_token)
                localStorage.setItem('username', value.data.username)
                localStorage.setItem('roles', value.data.roles)
                localStorage.setItem('refreshToken', value.data.refresh_token)
                router.push("/")
          })
    }
  }
}
</script>

<style scoped>

</style>