'use strict'

const Hapi = require('hapi')
var Vision = require('vision')

// create new server instance
const server = new Hapi.Server()

// add server’s connection information
server.connection({
  host: 'localhost',
  port: 3000
})

// register plugins to server instance
server.register([
  {
    register: require('vision')
  },
  {
    register: require('./index')
  }
], function (err) {
  if (err) {
    throw err
  }

  server.views({
    engines: {
      html: require('handlebars')
    },
    path: __dirname + '/views',
    layout: 'layout'
  })

  // start your server
  server.start(function (err) {

    if (err) {
      throw err
    }

    console.log('Server running at: ' + server.info.uri)
  })
})
