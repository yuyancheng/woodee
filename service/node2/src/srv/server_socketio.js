
const express = require('express');
const server = require('http').Server(express);
const IO = require('socket.io');
const api_loader = require('./../api/api-loader');
const sys = require('os');

const exp = express();
const Socket = IO(server);

Socket.on('connection', function (socket) {
    socket.emit('news', { hello: 'world' });
    socket.on('my other event', function (data) {
      console.log(data);
    });
});

module.exports = {
    run: (type) => {
        const srv = exp.listen(3030, function () {
            var address = srv.address();
            var host = address.address;
            var port = address.port;

            console.log(address);
            console.log(host);

            console.log('Server "' + type + '" started up at http://%s:%s', host, port);
        });

        api_loader.init(exp);
    }
};