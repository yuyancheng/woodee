(function (exports, require, module, __filename, __dirname) {
    'use strict';

    var connection = {};

    var MongoClient = require('mongodb').MongoClient,
        assert = require('assert'),
        DBName = 'wissy',
        URL = 'mongodb://localhost:27017/';


    const init = function (){
        var connect,
            db_tabs = [{
            name: 'restaurants'
        }];

        var insertDocument = function(db, callback) {
            db.collection(db_tabs[0].name).insertOne( {}, function(err, result) {
                assert.equal(err, null);
                console.log("Inserted a document into the restaurants collection.");
                callback();
            });
        };

        return loader;
    };

    function setDB(db){
        if(db){
            DBName = db;
        }
    }

    function setURL(url){
        if(url){
            URL = url;
        }
    }

    // 链接到数据库
    function connect(fun){
        MongoClient.connect(URL + DBName, function(err, db) {
            if(!db){
                console.error('Connection to database is not available!');
                return;
            }
            assert.equal(null, err);
            if(!err){
                console.log("Connected correctly to server.");
                fun(db);
            }else{
                console.error(err);
                return;
            }
        });
    }

    connection.findOne = function(sets, factor, fun){
        var gotOne = false;
        connect(function(db){
            var doc = db.collection(sets).findOne(factor, function (err, doc) {
                if(!err && doc !== undefined) {
                    fun(doc);
                    db.close();
                }else{
                    console.error(err);
                }
            });
        });
    };

    connection.findMany = function(sets, factor, fun){
        connect(function(db){
            var cursor = db.collection(sets).find(factor);
            cursor.toArray(function(err, doc){
                if(err === null){
                    fun(doc);
                    db.close();
                }
            });
        });
    };

    // 插入一条数据
    connection.insertOne = function(sets, dt, factor, fun){
        if(!dt){
            fun(null);
        }

        connect(function(db){
            db.collection(sets).insertOne(dt, null, function(err, result) {
                assert.equal(err, null);
                if(fun) {
                    fun(result);
                }
                db.close();
            });
        });
    };

    // 有则更新, 无则插入
    connection.insertOrUpdate = function(sets, dt, factor, fun){
        if(!dt){
            fun(null);
        }

        connect(function(db){
            var collection = db.collection(sets);
            collection.updateOne(factor, {$set: dt}, {upsert: true}, function (err, result) {
                assert.equal(err, null);
                if(fun) {
                    fun(result);
                }
                db.close();
            });
            return;
            collection.findOne(factor, function (err, doc) {
                assert.equal(err, null);
                if (err === null && doc) {
                    if(doc){
                        collection.updateOne(factor, {$set: dt}, {upsert: true});
                    }else{
                        collection.insertOne(dt, null, function(err, result) {
                            assert.equal(err, null);
                            if(fun) {
                                fun(result);
                            }
                            db.close();
                        });
                    }
                    db.close();
                }
            });
        });
    };

    // 插入多条数据
    connection.insertMany = function(sets, dt, factor, fun){
        if(!dt || dt.length === 0){
            fun(null);
        }

        connect(function(db){
            db.collection(sets).insertMany(dt, null, function(err, result) {
                assert.equal(err, null);
                if(fun) {
                    fun(result);
                }
                db.close();
            });
        });
    };

    module.exports = {
        init, 
        ...connection
    };

})(exports, require, module);