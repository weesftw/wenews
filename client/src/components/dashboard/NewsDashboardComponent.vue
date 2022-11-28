<template>
  <div class="row items-center justify-evenly">
    <q-card>
      <q-card-section class="q-pt-xl text-center">
        <p class="text-h5 text-bold">News</p>
      </q-card-section>
      <q-card-section>
        <div class="q-pa-md">
          <q-form @submit="onSubmitNews" class="q-gutter-md">
            <q-input filled v-model="title" label="Title" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="description" label="Description" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="author" label="Author" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="url" label="URL" type="url" hint="Link to article" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']"/>

            <q-input filled v-model="urlToImage" type="url" label="Image URL" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']" />

            <q-input filled v-model="category" label="Category" lazy-rules
                     :rules="[ val => val && val.length > 0 || 'Please type something']" />

            <div class="row justify-end">
              <q-btn type="submit" label="Submit" class="q-mt-md" color="primary" />
            </div>
          </q-form>
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>

<script lang="ts">
import {useQuasar} from 'quasar'
import NewsService from 'src/service/NewsService';
import {defineComponent, ref} from 'vue'
import {useCategoryStore} from 'stores/CategoryStore';
import {useRouter} from 'vue-router';

export default defineComponent({
  name: 'NewsDashboardComponent',
  setup() {
    const news = new NewsService();
    const { allCategories } = useCategoryStore();

    const router = useRouter()
    const $q = useQuasar()
    const title = ref('')
    const description = ref('')
    const author = ref('')
    const url = ref('')
    const date = ref('')
    const time = ref('')
    const urlToImage = ref('')
    const category = ref('')

    return {
      allCategories,
      title,
      description,
      author,
      url,
      date,
      time,
      urlToImage,
      category,
      onSubmitNews() {
        $q.loading.show()
        news.createNews({
          title: title.value,
          description: description.value,
          author: author.value,
          url: url.value,
          publishedAt: null,
          urlToImage: urlToImage.value,
          category: category.value
        }).then((value) => {
          $q.notify({
            color: 'positive',
            position: 'top',
            message: value.message,
            icon: 'report_problem'
          })

          router.push('/dashboard')
        }).catch(e => {
          $q.notify({
            color: 'negative',
            position: 'top',
            message: (<Error> e).message,
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

<style lang="sass" scoped>
.card-content
  padding-left: 100px
  padding-right: 100px

@media screen and (max-width: 699px)
  .card-content
    padding-left: 15px
    padding-right: 15px
</style>
