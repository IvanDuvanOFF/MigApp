const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({  
  devServer: {
    client: {
      overlay: {
        warnings: false,
        errors: false,
      },      
    }
  },
  transpileDependencies: true
})
