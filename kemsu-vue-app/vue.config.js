const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  devServer: {
    proxy: 'http://109.71.242.151/',
    client: {
      overlay: {
        warnings: false,
        errors: false,
      },      
      overlay: false,
    }
  },
  transpileDependencies: true
})
