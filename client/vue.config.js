const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8082,
    proxy: `http://${process.env.VUE_APP_BACKEND_URL}`,
  }
})
