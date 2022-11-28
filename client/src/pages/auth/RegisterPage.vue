<template>
  <q-page class="row items-center justify-evenly">
    <q-card class="my-card">
      <q-card-section class="q-pt-xl text-center">
        <p class="text-h5 text-bold">Register</p>
      </q-card-section>
      <q-card-section>
        <div class="q-pa-md">
          <q-form @submit="onSubmit" class="q-gutter-md">
            <q-input filled v-model="firstName" label="First name" hint="First name social" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="lastName" label="Last name" hint="Last name social" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="email" type="email" label="E-mail" hint="Personal e-mail" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="username" type="username" label="Username" hint="Username" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="password" type="password" label="Password" hint="Password" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="imageUrl" type="url" label="Image URL" hint="Image URL" />

            <q-toggle v-model="accept" label="I accept the license and terms"/>

            <div class="row justify-end">
              <q-btn type="submit" label="Submit" class="q-mt-md" color="primary" />
            </div>
          </q-form>
        </div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script lang="ts">
import {useQuasar} from 'quasar'
import {userService} from 'src/service/UserService'
import {defineComponent, ref} from 'vue'
import {useRouter} from 'vue-router';

export default defineComponent({
  name: 'RegisterPage',
  setup() {
    const $q = useQuasar()
    const router = useRouter()
    const firstName = ref('')
    const lastName = ref('')
    const email = ref('')
    const imageUrl = ref(null)
    const username = ref('')
    const password = ref('')
    const accept = ref(false)

    return {
      firstName,
      lastName,
      email,
      imageUrl,
      username,
      password,
      accept,
      onSubmit() {
        if (accept.value == true) {
          $q.loading.show()
          userService.registerUserRequest({
            firstName: firstName.value,
            lastName: lastName.value,
            email: email.value,
            imageUrl: <string><unknown>imageUrl.value,
            username: username.value,
            password: password.value,
          }).then((value) => {
            $q.notify({
              color: 'positive',
              position: 'top',
              message: value.message,
              icon: 'report_problem'
            })

            router.push('/login')
          }).catch(e => {
            $q.notify({
              color: 'negative',
              position: 'top',
              message: e.message,
              icon: 'report_problem'
            })
          }).finally(() => {
            $q.loading.hide()
          });
        } else {
          $q.notify({
            color: 'red-5',
            textColor: 'white',
            position: 'top',
            icon: 'warning',
            message: 'You need to accept the license and terms first'
          })
        }
      }
    }
  }
});
</script>
