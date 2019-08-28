"use strict";

const config = require('./config');
// const server = require('./../srv/server')();
const server_socketio = require('./../srv/server_socketio');
let db_loader = require('./../db/db-loader');
const api_loader = require('./../api/api-loader');

if(server_socketio){
    server_socketio.run('srv64');
}else{
    server.run('srv64');
    // server.run('srv64', api_loader);
}

const api = db_loader.setDB('mysql');
db_loader = {...api};