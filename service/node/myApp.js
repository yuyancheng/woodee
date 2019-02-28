var express = require('express');
var app = express();
var bodyParser = require("body-parser");

app.use(bodyParser.urlencoded({
    extended: false
}));

app.get('/name/getName', function (req, res) {
    var headerStr = '';
    //for(var k in req){ headerStr += k + ':   ' + typeof req[k] + '<br/>';}
    for (var k in req.query) {
        headerStr += k + ':   ' + typeof req.query[k] + '<br/>';
    }
    //res.send(headerStr);
    if (req.query.id == 10000 && req.query.type == 'people') {
        res.send('Hello YYC');
    } else {
        res.send('Not Found! ' + req.query.id);
    }

});

app.post('/test', function (req, res) {
    var headerStr = '';
    for(var k in req){ headerStr += k + ':   ' + req[k] + '\n';}
    res.send(headerStr);
});

app.post('/info/setInfo', function(req, res) {
    if(req.body.id == 100){
        res.send('OK');
    }else{
        res.send('NO');
    }
});

app.post('/test2', function(req, res) {
    var headerStr = '';
    for(var k in req.body){ headerStr += k + ':   ' + req.body[k] + '\n';}
    res.send(headerStr);
});

var server = app.listen(3000, function () {
    var host = server.address().address;
    var port = server.address().port;

    console.log('Example app listening at http://%s:%s', host, port);
});