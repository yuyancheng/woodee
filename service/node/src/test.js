(function (exports, require, module, __filename, __dirname) {
    'use strict';

    //const buf2 = new Buffer('7468697320697320612074c3a97374', 'hex');
    //console.log(buf2.toString());

    const arr = [Buffer('1234'), Buffer('0123')];
    //arr.sort(Buffer.compare);
    //console.log(arr.sort(Buffer.compare));

    const buf1 = new Buffer(10).fill(0);
    const buf2 = new Buffer(14).fill(0);
    const buf3 = new Buffer(18).fill(0);
    const totalLength = buf1.length + buf2.length + buf3.length;

    //console.log(totalLength);
    const bufA = Buffer.concat([buf1, buf2, buf3], totalLength);
    //console.log(bufA);
    //console.log(bufA.length);

    const buf_a = new Buffer('12345');
    const buf_b = new Buffer(buf_a.length);
    var len = buf_a.copy(buf_b, 1, 0, buf_a.length);
    //const buf_c = new Buffer('abc');

    //console.log(buf_b);

    /*console.log('System: EOL >> ' + sys.EOL);
     console.log('System: arch >> ' + sys.arch());
     console.dir('System: cpus >> ' + sys.cpus());
     console.log('System: endianness >> ' + sys.endianness());
     console.log('System: freemem >> ' + sys.freemem());
     console.log('System: homedir >> ' + sys.homedir());
     console.log('System: hostname >> ' + sys.hostname());
     console.log('System: loadavg >> ' + sys.loadavg());
     console.dir('System: networkInterfaces >> ' + sys.networkInterfaces());*/
    
})(exports, require, module);

