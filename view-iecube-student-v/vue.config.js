'use strict'
const path = require('path')

function resolve(dir) {
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
      // changOrigin: true,
      '/dev-api': {
        target: 'http://192.168.8.108:9094',
        // target: 'http://[::1]:9191',
        pathRewrite: {
          '^/dev-api': ''
        }
      },
      '/local-resource': {
        target: 'http://192.168.8.108:9094'
        // target: 'http://[::1]:9191',
      },
      // 'https://open.ys7.com': {
      //   'target': 'https://open.ys7.com',
      //   'secure': true, // false为http访问，true为https访问
      //   'changeOrigin': true, // 跨域访问设置，true代表跨域
      //   'pathRewrite': { // 路径改写规则
      //     '^https://open.ys7.com': ''
      //   }
      // }
    },
    // before: require('./mock/mock-server.js')
  },
  chainWebpack(config) {
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
