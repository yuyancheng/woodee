(function (exports, require, module, __filename, __dirname) {
    'use strict';

    var express = require('express');
    var exp = express();


    function Server(){
        var server = {};
        server.run = function(type, param){
            if(param && param.constructor === Array){
                var len = param.length;
                while( len -- ){
                    var md = require(param[len]);
                    if(md && md.init){
                        md.init(exp);
                    }
                }

                //param.init(exp);

            }

            var srv = exp.listen(3030, function () {
                var host = srv.address().address;
                var port = srv.address().port;

                console.log('Server "srv64" started up at http://%s:%s', host, port);
            });

        };

        return server;
    }

    module.exports = Server;

    //exports.srv32 = srv32;
    //exports.srv64 = srv64;

    /*srv64.run = function(loader){
        exp.listen(3000, function () {
            var host = srv64.server.address().address;
            var port = srv64.server.address().port;

            console.log('Example app listening at http://%s:%s', host, port);
        });

        if(loader && loader.init){
            loader.init(exp);
        }
    };*/

    /*srv64.server = exp.listen(3000, function () {
        var host = srv64.server.address().address;
        var port = srv64.server.address().port;

        console.log('Example app listening at http://%s:%s', host, port);
    });*/

    //module.exports.srv64 = srv64;
    //module.exports.exp = exp;
})(exports, require, module);

