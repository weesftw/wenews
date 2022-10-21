<template>
  <q-page class="row items-center justify-evenly">
    <q-card class="my-card">
      <q-card-section class="q-pt-xl text-center">
        <p class="text-h5 text-weight-bolder">WhatIsHappen</p>
        <p class="text-body1">Sign In</p>
      </q-card-section>
      <q-card-section>
        <div class="q-pa-md">

          <q-form
            @submit="onSubmit"
            class="q-gutter-md"
          >
            <q-input
              filled
              v-model="username"
              label="Username"
              lazy-rules
              :rules="[ val => val && val.length > 0 || 'Please type something']"
            />

            <q-input
              filled
              v-model="password"
              type="password"
              label="Password"
              hint="We'll never share your password with anyone else."
              lazy-rules
              :rules="[ val => val && val.length > 0 || 'Please type something']"
            />

            <div class="row justify-end">
              <q-btn
                type="submit"
                label="Submit"
                class="q-mt-md"
                color="primary"
              >
                <template v-slot:loading>
                  <q-spinner-facebook/>
                </template>
              </q-btn>
            </div>

            <div class="q-mt-lg row justify-end">
              <p>Don't you have an account?
                <router-link to="/register">Register Now</router-link>
              </p>
            </div>
          </q-form>
        </div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script lang="ts">
import {useQuasar} from 'quasar';
import {api} from 'src/boot/axios';
import {defineComponent, ref} from 'vue';
import {useRouter} from 'vue-router';

export default defineComponent({
  name: 'LoginPage',

  setup() {
    const $q = useQuasar()
    const router = useRouter()

    const username = ref(null)
    const password = ref(null)
    const accept = ref(false)

    return {
      username,
      password,
      accept,

      onSubmit() {
        const result = api.post('/core/login', {'username': username.value, 'password': password.value});
        result.then((response) => {
          if (response.status === 200) {
            $q.localStorage.set('token', response.data.access_token)
            $q.localStorage.set('username', response.data.username)
            $q.localStorage.set('roles', response.data.roles)
            $q.localStorage.set('refreshToken', response.data.refresh_token)

            $q.notify({
              color: 'positive',
              position: 'top',
              message: 'Successfully logged in',
              icon: 'report_problem'
            })

            router.push({ path: '/' })
          }
        }).catch((response) => {
          $q.notify({
            color: 'negative',
            position: 'top',
            message: response.response.data.message,
            icon: 'report_problem'
          })
        });
      }
    }
  }
});
</script>

<style scoped>

</style>
