(function (exports, require, module, __filename, __dirname) {
    'use strict';

    // 请求结果状态码
    var code = {
        'OK': '1111',
        'ERROR': '1110',
        'WARNING': '1110',
        'EXIST': '1100',
        'NOT_FOUND': '0000'
    };

    module.exports = code;

})(exports, require, module);