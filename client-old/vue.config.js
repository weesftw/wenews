const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8085,
    proxy: {
      '/core': {
        target: `http://${process.env.VUE_APP_CORE_BACKEND_URL}`,
        pathRewrite: { '^/core': '' }
      },
      '/news': {
        target: `http://${process.env.VUE_APP_NEWS_BACKEND_URL}`,
        pathRewrite: { '^/news': '' }
      }
    }
  }
})
