'use strict'

const Handler = require('./handler')

const Routes = [
  {
    method: 'GET',
    path: '/',
    config: Handler.index
  },
  {
    method: 'GET',
    path: '/verifyLinks',
    config: Handler.verifyLinks
  },
  {
    method: 'POST',
    path: '/analyze',
    config: Handler.result
  }
]
module.exports = Routes
