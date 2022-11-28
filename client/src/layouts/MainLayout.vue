<template>
  <q-layout view="hHh lpR fFf" class="bg-grey-1">
    <q-header elevated class="bg-white text-grey-8" height-hint="64">
      <q-toolbar class="GNL__toolbar" style="height: 64px">
        <q-btn
          flat
          dense
          round
          @click="toggleLeftDrawer"
          aria-label="Menu"
          icon="menu"
          class="q-mr-sm"
        />

        <q-toolbar-title v-if="$q.screen.gt.xs" shrink class="row items-center no-wrap">
          <img src="we.png">
          <span class="q-ml-sm">News</span>
        </q-toolbar-title>

        <q-space />

        <q-input class="GNL__toolbar-input" outlined dense v-model="search" color="bg-grey-7 shadow-1" placeholder="Search for topics, locations & sources">
          <template v-slot:prepend>
            <q-icon v-if="search === ''" name="search" />
            <q-icon v-else name="clear" class="cursor-pointer" @click="search = ''" />
          </template>
        </q-input>

        <q-space />

        <AccountComponent />
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      class="bg-white"
      :width="280"
    >
      <q-scroll-area class="fit">
        <q-list padding class="text-grey-8">
          <q-item class="GNL__drawer-item" :to="m.link" v-for="m in main" :key="m.text" clickable>
            <q-item-section avatar>
              <q-icon :name="m.icon" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ m.text }}</q-item-label>
            </q-item-section>
          </q-item>

          <q-separator inset class="q-my-sm" />

          <div v-if="categories.length === 0" class="q-mt-md">
            <div class="flex flex-center q-gutter-xs">
              <q-spinner-dots color="black" size="2em" />
            </div>
          </div>

          <q-item :disable="getUser?.banned" v-else class="GNL__drawer-item" :to="/news/ + category.name.toString().toLowerCase()" v-for="category in categories" :key="category.id" clickable>
            <q-item-section avatar>
              <q-icon name="tag" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ category.name }}</q-item-label>
            </q-item-section>
          </q-item>

          <q-expansion-item
            expand-separator
            icon="dynamic_feed"
            label="Others"
          >
            <div v-if="categories.length === 0" class="q-mt-md">
              <div class="flex flex-center q-gutter-xs">
                <q-spinner-dots color="black" size="2em" />
              </div>
            </div>

            <q-item :disable="getUser?.banned" v-else class="GNL__drawer-item" :to="/news/ + category.name.toString().toLowerCase()" v-for="category in externalCategories" :key="category.id" clickable>
              <q-item-section avatar>
                <q-icon name="tag" />
              </q-item-section>
              <q-item-section>
                <q-item-label>{{ category.name }}</q-item-label>
              </q-item-section>
            </q-item>
          </q-expansion-item>

          <q-separator inset class="q-my-sm" />

          <q-item class="GNL__drawer-item" :href="op.link" target="_blank" v-for="op in options" :key="op.text" clickable>
            <q-item-section>
              <q-item-label>{{ op.text }} <q-icon v-if="op.icon" :name="op.icon" /></q-item-label>
            </q-item-section>
          </q-item>

          <div class="q-mt-md">
            <div class="flex flex-center q-gutter-xs">
              <a class="GNL__drawer-footer-link" href="javascript:void(0)" aria-label="Privacy">Privacy</a>
              <span> · </span>
              <a class="GNL__drawer-footer-link" href="javascript:void(0)" aria-label="Terms">Terms</a>
              <span> · </span>
              <a class="GNL__drawer-footer-link" href="javascript:void(0)" aria-label="About">About</a>
            </div>
          </div>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue'
import AccountComponent from 'components/AccountComponent.vue'
import {useCategoryStore} from 'stores/CategoryStore';
import { storeToRefs } from 'pinia';
import { useUserStore } from 'src/stores/UserStore';

export default defineComponent({
  name: 'MainLayout',
  components: {AccountComponent},

  setup() {
    const userStore = useUserStore();
    const categoryStore = useCategoryStore();
    const search = ref('')
    const leftDrawerOpen = ref(false)
    const main = ref([
      {icon: 'person', text: 'For you', link: '/'},
      {icon: 'star_border', text: 'Favorites', link: null}
    ])
    const options = ref([
      {icon: '', text: 'Settings', link: null},
      {icon: 'open_in_new', text: 'GitHub', link: 'https://github.com/weesftw/wenews'}
    ])
    const { categories, externalCategories } = storeToRefs(categoryStore);
    const { isAuth, getUser } = storeToRefs(userStore);

    userStore.fetchUser();
    categoryStore.fetchCategory();

    function toggleLeftDrawer() {
      leftDrawerOpen.value = !leftDrawerOpen.value
    }

    return {
      main,
      isAuth,
      getUser,
      options,
      search,
      categories,
      leftDrawerOpen,
      toggleLeftDrawer,
      categoryStore,
      externalCategories
    }
  }
});
</script>

<style lang="sass">
.GNL
  &__toolbar
    height: 64px
  &__toolbar-input
    width: 55%
  &__drawer-item
    line-height: 24px
    border-radius: 0 24px 24px 0
    margin-right: 12px
    .q-item__section--avatar
      .q-icon
        color: #5f6368
    .q-item__label
      color: #3c4043
      letter-spacing: .01785714em
      font-size: .875rem
      font-weight: 500
      line-height: 1.25rem
  &__drawer-footer-link
    color: inherit
    text-decoration: none
    font-weight: 500
    font-size: .75rem
    &:hover
      color: #000
</style>
