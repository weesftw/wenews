<template>
  <HeaderBar/>
  <main>
    <div class="row">
      <div class="col-lg-12">
        <div class="card col-lg-12 shadow text-center">
          <div class="card-header fw-bolder"></div>
          <div class="card-body">

          </div>
          <div class="card-footer text-muted text-end"></div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import HeaderBar from "@/components/HeaderBar";

export default {
  name: "DashboardView",
  components: {
    HeaderBar,
  },

  data() {
    return {

    }
  },

  methods: {

  }
}
</script>

<style scoped>

</style>