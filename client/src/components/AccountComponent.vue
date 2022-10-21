<template>
  <q-btn round flat>
    <q-avatar size="26px">
      <img src="https://yt3.ggpht.com/ytc/AMLnZu-15DIYEfL5o7qFG7ukgIDfwVc9pxhXtxGL6Bkw=s900-c-k-c0x00ffffff-no-rj">
    </q-avatar>
    <q-tooltip>Account</q-tooltip>
    <q-menu
      transition-show="jump-down"
      transition-hide="jump-up"
      @update:model-value="authenticated"
    >
      <q-list v-if="authenticated" style="min-width: 100px">
        <q-item clickable v-close-popup>
          <q-item-section>Dashboard</q-item-section>
        </q-item>
        <q-separator />
        <q-item clickable @click="logout" v-close-popup>
          <q-item-section>Logout</q-item-section>
        </q-item>
      </q-list>
      <q-list v-else style="min-width: 100px">
        <q-item clickable to="/login" exact>
          <q-item-section>Login</q-item-section>
        </q-item>
      </q-list>
    </q-menu>
  </q-btn>
</template>

<script lang="ts">
import { userService } from 'src/service/UserService';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'AccountComponent',

  data() {
    return {
      authenticated: ref(userService.isSignedIn())
    }
  },
  methods: {
    logout() {
      if(userService.signOut())
        this.authenticated = false;
    }
  }
});
</script>
