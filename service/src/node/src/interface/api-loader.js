(function (exports, require, module, __filename, __dirname) {
    'use strict';

    var modules = require('./config'),
        bodyParser = require("body-parser"),
        apiLoader = {},
        exp;



    apiLoader.init = function (express){

        exp = express;

        if(!exp) {
            console.log('Express is not loaded!');
            return;
        }

        exp.use(bodyParser.urlencoded({
            extended: false
        }));

        var len = modules.length;

        // 遍历每个模块，初始化所有对外接口
        while( len -- ){
            var md = require(modules[len]);
            if(!md || !md.APIs) {
                continue;
            }
            var ln = md.APIs.length;
            for(var i=0; i<ln; i++){
                var api = md.APIs[i];
                if(!api.type || api.type.length === 0){
                    api.type = ['post'];
                }
                var l = api.type.length;
                for(var j=0; j<l; j++){
                    switch (api.type[j]){
                        case 'get':
                            if(api.path && api.fun) {
                                exp.get(api.path, api.fun.ref);
                            }
                            break;
                        case 'post':
                            if(api.path && api.fun) {
                                exp.post(api.path, api.fun.ref);
                            }
                            break;
                        case 'delete':
                            if(api.path && api.fun) {
                                exp.delete(api.path, api.fun.ref);
                            }
                            break;
                        case 'update':
                            if(api.path && api.fun) {
                                exp.update(api.path, api.fun.ref);
                            }
                            break;
                        default: break;
                    }
                }
            }

            if(md.name){
                console.log('Interface [' + md.name + '] heave been initialized!');
            }
        }
    };

    module.exports = apiLoader;

})(exports, require, module);