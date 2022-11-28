<template>
  <div class="q-pa-md row items-center justify-evenly q-gutter-md">
    <q-card style="width: 100%; max-width: 450px" flat bordered v-for="article in articles" :key="article.id">
      <img :src="article.urlToImage" alt="">

      <q-card-section>
        <div class="text-h6">{{article.title}}</div>
        <div class="text-subtitle2 q-mt-sm">{{article.author}} ({{article.publishedAt}})</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        {{article.description}}
      </q-card-section>
      <q-separator />

      <q-card-actions class="row justify-end">
        <q-btn :href="article.url" target="_blank" flat color="primary">
           More information
        </q-btn>
      </q-card-actions>
    </q-card>
  </div>
</template>

<script lang="ts">
import {defineComponent, PropType, ref} from 'vue';
import {News} from 'src/service/SocketService';

export default defineComponent({
  name: 'NewsComponent',
  props: {
    articles: {
      type: Array as unknown as PropType<News[]>,
      required: true
    }
  },

  setup() {
    return {
      tab: ref('news'),
      splitterModel: ref(20)
    }
  }
})
</script>

<style lang="sass" scoped>

.custom-caption
  text-align: center
  padding: 12px
  color: white
  background-color: rgba(0, 0, 0, .3)

@media screen and (max-width: 699px)
  .custom-caption
    padding-left: 15px
    padding-right: 15px

</style>
