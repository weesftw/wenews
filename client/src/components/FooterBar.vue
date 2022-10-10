<template>
  <footer class="footer bg-dark mt-auto">
    <div class="container">
      <div class="row">
        <div class="col-lg-6 h-100 text-center text-lg-start my-auto">
          <p class="text-muted small mb-4 mb-lg-0">&copy; {{ projectName }} Website 2022. All Rights Reserved.</p>
        </div>
        <div class="col-lg-6 h-100 text-center text-lg-end my-auto">
          <ul class="list-inline mb-0">
            <li class="list-inline-item">
              <a href="https://github.com/weesftw/whatishappen"><i class="bi-github text-light fs-3"></i></a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: "FooterBar",

  data() {
    return {
      projectName: process.env.VUE_APP_TITLE,
    }
  }
}
</script>

<style scoped>
.footer
{
  padding-top: 4rem;
  padding-bottom: 4rem;
}

</style>