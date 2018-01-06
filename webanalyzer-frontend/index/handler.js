'use strict'

var Request = require('request'),
    HttpStatus = require('http-status-codes');

const Handler = {
  index: {
    handler: function (request, reply) {
      reply.view('index')
    }
  },
  result : {
    handler: function (request, reply) {

      var options = {
          method: 'POST',
          body: request.payload,
          json: true,
          url: 'http://localhost:8080/analyze',
      };

      Request(options, function (error, response, body) {
          var statusCode = response ? response.statusCode : HttpStatus.SERVICE_UNAVAILABLE;
          console.log(response.body);
          reply.view('analysisresult', response.body);
      });
    }
  },
  verifyLinks: {
    handler: function (request, reply) {
      var options = {
          method: 'GET',
          json: true,
          url: 'http://localhost:8080/verifyLinks?url='+ request.query.url +'&startRecord='+request.query.startRecord+'&limit=10',
      };

      Request(options, function (error, response, body) {
          var statusCode = response ? response.statusCode : HttpStatus.SERVICE_UNAVAILABLE;
          reply(response.body);
      });
    }
  },
}
module.exports = Handler
