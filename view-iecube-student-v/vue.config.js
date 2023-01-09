'use strict'
const path = require('path')

function resolve (dir) {
  return path.join(__dirname, dir)
}

const port = process.env.port || process.env.npm_config_port || 8081 // dev port

module.exports = {
  lintOnSave: process.env.NODE_ENV === 'development',
  devServer: {
    port: port,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      '/dev-api': {
        target: 'http://47.94.161.154:9091',
        pathRewrite: {
          '^/dev-api': ''
        }
      },
      '/local-resource': {
        target: 'http://47.94.161.154:9091'
      }
    }
    // before: require('./mock/mock-server.js')
  },
  chainWebpack (config) {
    config.module
      .rule('icons')
      .test(/\.js$/)
      .include.add(resolve('node_modules/pdfjs-dist'))
      .end()
      .use('babel-loader')
      .loader('babel-loader')
      .end()
  }
}
