(function (exports, require, module, __filename, __dirname) {
    'use strict';

    var code = require('./../common/config'),
        admin = {
            name: 'System Administration'
        };

    admin.APIs = [
        { 'path': '/sys/setAsAdmin', type: ['post'], 'fun': setAsAdmin },
        { 'path': '/sys/removeAdmin', type: ['delete'], 'fun': removeAdmin  }
    ];

    admin.init = function (exp){
        var len = APIs.length;
        for(var i=0; i<len; i++){
            exp.get(APIs[i].path, APIs[i].fun);
            exp.post(APIs[i].path, APIs[i].fun);
        }
    };

    function setAsAdmin (req, res){
        if(req.query.name === 'YYC' && req.query.pwd === '123456'){
            res.send( code.OK );
        }else{
            res.send( code.ERROR );
        }
    };

    function removeAdmin (req, res){
        //console.log(req);
        res.send( code.OK );
    };

    module.exports = admin;
    module.exports.login = setAsAdmin;
    module.exports.logout = removeAdmin;

})(exports, require, module);