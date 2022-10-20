<template>
  <HeaderBar/>
  <main>
    <div class="row">
      <div class="col">
        <div class="card shadow">
          <div class="card-header text-center fw-bolder">
            Register
          </div>
          <div class="card-body">
            <div class="d-flex justify-content-center">
              <div class="row g-1 col-lg-5">
                <div v-if="hasError" class="alert alert-danger" role="alert">{{ hasError }}</div>
                <div class="mb-3 col-6 form-floating">
                  <input v-model="user.firstName" type="text" class="form-control" placeholder="First Name">
                  <label for="firstName">First Name</label>
                  <div class="invalid-feedback"></div>
                </div>
                <div class="mb-3 col-6 form-floating">
                  <input v-model="user.lastName" type="text" class="form-control" placeholder="Last Name">
                  <label for="lastName">Last Name</label>
                  <div class="invalid-feedback"></div>
                </div>
                <div class="mb-3 form-floating">
                  <input v-model="user.email" type="email" class="form-control" placeholder="example@example.com">
                  <label for="email">E-mail</label>
                  <div class="invalid-feedback"></div>
                </div>
                <div class="mb-3 col-6 form-floating">
                  <input v-model="user.username" type="text" class="form-control" placeholder="foo">
                  <label for="text">Username</label>
                  <div class="invalid-feedback"></div>
                </div>
                <div class="mb-3 col-6 form-floating">
                  <input v-model="user.password" type="password" class="form-control" placeholder="foo">
                  <label for="password">Password</label>
                  <div class="invalid-feedback"></div>
                </div>
                <div class="gutters text-end">
                  <button @click="createUser" type="submit" class="btn btn-danger">Create</button>
                </div>
              </div>
            </div>
          </div>
          <div class="card-footer text-muted text-end"></div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import HeaderBar from '@/components/HeaderBar.vue'
import User from '@/model/User'
import UserService from "@/service/UserService";
import router from "@/router";

export default {
  name: "RegisterView",
  components: {
    HeaderBar
  },

  data() {
    return {
      hasError: null,
      user: {
        firstName: '',
        lastName: '',
        email: '',
        username: '',
        password: ''
      }
    }
  },

  methods: {
    createUser() {
      UserService.createUser(new User(this.user.firstName, this.user.lastName, this.user.email, this.user.username, this.user.password))
          .catch(reason => {
            this.hasError = reason.response.data.errors[0];
          }).then(value => {
            if(value != null)
              if (value.status === 201)
                router.back();
      });
    }
  }
}
</script>

<style scoped>

</style>