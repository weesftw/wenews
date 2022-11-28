<template>
  <div v-if="isAuth" class="q-gutter-sm row items-center no-wrap">
    <q-btn round dense flat color="grey-8" icon="notifications">
      <q-badge color="red" text-color="white" floating>
        1
      <!--        put value here when has notifications        -->
      </q-badge>
      <q-tooltip>Notifications</q-tooltip>
    </q-btn>
    <q-btn round flat>
      <q-avatar size="26px">
        <q-img
          :src="getUser.imageUrl == null ? 'profile.png' : getUser.imageUrl" />
      </q-avatar>
      <q-tooltip>Account</q-tooltip>
      <q-menu
        transition-show="jump-down"
        transition-hide="jump-up"
      >
        <q-list style="min-width: 100px">
          <q-item to="/dashboard" v-close-popup>
            <q-item-section>Dashboard</q-item-section>
          </q-item>
          <q-separator />
          <q-item clickable label="Confirm" @click="confirm = true" v-close-popup>
            <q-item-section>Logout</q-item-section>
          </q-item>
        </q-list>
      </q-menu>
    </q-btn>
  </div>

  <q-btn v-else color="primary" clickable to="/login" exact label="Login" style="width: 100px" />

  <q-dialog v-model="confirm" persistent>
    <q-card>
      <q-card-section class="row items-center">
        <q-avatar icon="logout" color="primary" text-color="white" />
        <span class="q-ml-sm">Do you really want to leave?</span>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancel" color="primary" v-close-popup />
        <q-btn flat label="Logout" @click="logout" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script lang="ts">
import { userService } from 'src/service/UserService';
import {defineComponent, ref } from 'vue';
import {useUserStore} from 'stores/UserStore';
import {useRouter} from 'vue-router';
import { storeToRefs } from 'pinia';

export default defineComponent({
  name: 'AccountComponent',

  setup() {
    const router = useRouter();
    const userState = useUserStore()
    const { getUser, isAuth } = storeToRefs(userState)

    return {
      router,
      userState,
      getUser,
      isAuth,
      confirm: ref(false)
    }
  },

  methods: {
    logout() {
      userService.signOut()
      this.router.push('/')
    }
  }
});
</script>
