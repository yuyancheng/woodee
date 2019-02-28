(function (exports, require, module, __filename, __dirname) {
    'use strict';

    var config = require('./config');
    var server = require('./../srv/server')();
    var db_loader = require('./../db/db-loader');
    var api_loader = require('./../interface/api-loader');

    if(server){
        server.run('srv64');
    }else{
        server.run('srv64', api_loader);
    }

}(exports, require, module));

