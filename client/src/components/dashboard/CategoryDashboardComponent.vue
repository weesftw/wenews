<template>
  <div class="row items-center justify-evenly">
    <q-card>
      <q-card-section class="q-pt-xl text-center">
        <p class="text-h5 text-bold">Category</p>
      </q-card-section>
      <q-card-section>
        <div class="q-pa-md">

          <q-form
            @submit="onSubmitCategory"
            class="q-gutter-md"
          >
            <q-input
              filled
              v-model="category"
              label="Category"
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

export default defineComponent({
  name: 'CategoryDashboardComponent',
  setup() {
    const news = new NewsService();

    const $q = useQuasar()
    const category = ref('')

    return {
      category,
      onSubmitCategory() {
        $q.loading.show()
        news.createCategory({
          name: category.value,
          password: null
        }).then((value) => {
          $q.notify({
            color: 'positive',
            position: 'top',
            message: value.message,
            icon: 'report_problem'
          })
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
