<template>
  <HeaderBar />
  <main>
    <div class="row">
      <div class="col-lg-12">
        <div class="card col-lg-12 shadow">
          <div class="card-header fw-bolder text-center">
            Choose category
          </div>
          <div class="card-body text-center">
            <div class="row justify-content-center">
              <div class="col-lg-5">
                <ol class="list-group list-group-numbered">
                  <li v-for="category in categories" v-bind:key="category.id" class="list-group-item d-flex justify-content-between align-items-start">
                    <div class="ms-2 me-auto">
                      <div class="fw-bold">{{ category.name[0].toUpperCase() + category.name.toLowerCase().slice(1) }}</div>
                    </div>
                    <span class="badge bg-danger rounded-pill">{{ category.count }}</span>
                  </li>
                </ol>
              </div>
            </div>
            <div class="mt-3 counter-box colored text-center">
              <p><span class="counter fw-bold">{{ totalOnline }}</span> Members Online<br>
                Enter a category to start chatting:
              </p>
            </div>
            <div class="col-sm-4 mx-auto">
              <select @change="handleCategory($event)" class="form-select" aria-label="Default select example">
                <option selected disabled>Select one category</option>
                <option v-for="category in categories" v-bind:key="category.id" >{{ category.name[0].toUpperCase() + category.name.toLowerCase().slice(1) }}</option>
              </select>
            </div>
            <button id="joinUs" v-bind:disabled="!selected" v-on:click="sendToChat" class="mt-3 btn btn-danger fw-bold">Join us</button>
          </div>
          <div class="card-footer text-muted text-end"></div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import HeaderBar from '../components/HeaderBar.vue'
import CategoryService from '../service/CategoryService'
import router from "@/router";

export default {
  name: "HomeView",
  components: {
    HeaderBar
  },

  data() {
    return {
      totalOnline: 0,

      selected: null,
      categories: []
    }
  },

  mounted() {
    CategoryService.getAll().then(value => {
      this.totalOnline = 0;
      this.categories = value.data;
    });
  },

  methods: {
    handleCategory: function(e) {
      this.selected = e.target.value;
    },

    sendToChat: function() {
      router.push({ path: '/chat', query: { q: this.selected.toLowerCase() }});
    }
  }
}
</script>

<style scoped>
select
{
  border: none;
  outline: none;
  background-color: #E9ECEF;
}

i.bi-people-fill
{
  font-size: 5rem;
}

.features-icons
{
  padding-top: 7rem;
  padding-bottom: 7rem;
}

.features-icons .features-icons-item .features-icons-icon i
{
  font-size: 4.5rem;
}
</style>