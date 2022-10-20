<template>
  <q-page class="row items-center justify-evenly">
    <q-card class="my-card">
        <q-card-section class="q-pt-xl text-center">
          <p class="text-h5 text-weight-bolder">WhatIsHappen</p>
          <p class="text-body1">Sign In</p>
        </q-card-section>
      <q-card-section>
        <div class="q-pa-md" style="width: 400px">

          <q-form
            @submit="onSubmit"
            @reset="onReset"
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
              label="Password"
              hint="We'll never share your password with anyone else."
              lazy-rules
              :rules="[ val => val && val.length > 0 || 'Please type something']"
            />

            <q-toggle v-model="accept" label="I accept the license and terms"/>

            <div class="row justify-end">
              <q-btn
                type="submit"
                :loading="submitting"
                label="Submit"
                class="q-mt-md"
                color="primary"
              >
                <template v-slot:loading>
                  <q-spinner-facebook/>
                </template>
              </q-btn>
            </div>

            <div class="row justify-end">
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
import {defineComponent} from 'vue';

export default defineComponent({
  name: 'LoginPage',

  data() {
    return {
      accept: false,
      username: '',
      password: ''
    }
  },
});
</script>

<style scoped>

</style>
