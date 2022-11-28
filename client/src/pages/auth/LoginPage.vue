<template>
  <q-page class="row items-center justify-evenly">
    <q-card class="my-card">
      <q-card-section class="q-pt-xl text-center">
        <p class="text-h5 text-bold">Sign In</p>
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
              />
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
import {useQuasar} from 'quasar'
import {defineComponent, ref} from 'vue'
import {userService} from 'src/service/UserService'
import {useRouter} from 'vue-router';

export default defineComponent({
  name: 'LoginPage',
  setup() {
    const $q = useQuasar()
    const router = useRouter()
    const username = ref('')
    const password = ref('')

    return {
      username,
      password,
      onSubmit() {
        $q.loading.show()
        userService.signInRequest({
          username: username.value, password: password.value
        }).then(value => {
          $q.notify({
            color: 'positive',
            position: 'top',
            message: value.message,
            icon: 'report_problem'
          })

          router.push('/')
        }).catch(reason => {
          $q.notify({
            color: 'negative',
            position: 'top',
            message: reason.message,
            icon: 'report_problem'
          })
        }).finally(() => {
          $q.loading.hide()
        });
      }
    }
  }
});
</script>
