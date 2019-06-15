"use strict";

var connection = {};

var mysqlConnection = require("mysql").createConnection({
  host: "127.0.0.1",
  user: "root",
  password: "123456",
  database: "wissy"
});

// mysqlConnection.connect();

// 连接到数据库
mysqlConnection.connect();

function connect(fun) {

  fun();

  // 断开数据库连接
  // mysqlConnection.end();
}

connection.findOne = (sets, factor, fun) => {
  var gotOne = false;
  connect(function() {
    const qStr = `SELECT * from ${sets} ${factor}`;
    mysqlConnection.query(qStr, function (error, results, fields) {
      if (error) throw error;
      console.log('The solution is: ', results[0]);
      fun(results[0]);
    });
  });
};

connection.findMany = (sets, factor, fun) => {
  connect(function(db) {
    mysqlConnection.query(`SELECT * from ${sets}`, function (error, results, fields) {
      if (error) throw error;
      console.log('The solution is: ', results[0]);
      fun(results[0]);
    });
  });
};

// 插入一条数据
connection.insertOne = (sets, dt, factor, fun) => {
  if (!dt) {
    fun(null);
  }

  connect(function() {
    mysqlConnection.query(`INSERT INTO ${sets}(${factor}) VALUES(0,?,?,?)`, dt, function (error, results, fields) {
      if (error) throw error;
      let result = null;
      if (results.affectedRows) {
        result = {
          ok: true,
          data: {
            id: results.insertId
          }
        };
      }
      fun(result);
    });
  });
};

// 有则更新, 无则插入
connection.insertOrUpdate = (sets, dt, factor, fun) => {
  if (!dt) {
    fun(null);
  }

  connect(function(db) {
    var collection = db.collection(sets);
    collection.updateOne(factor, { $set: dt }, { upsert: true }, function(err, result) {
      assert.equal(err, null);
      if (fun) {
        fun(result);
      }
      db.close();
    });
    return;
  });
};

// 插入多条数据
connection.insertMany = (sets, dt, factor, fun) => {
  if (!dt || dt.length === 0) {
    fun(null);
  }

  connect(function(db) {
    db.collection(sets).insertMany(dt, null, function(err, result) {
      assert.equal(err, null);
      if (fun) {
        fun(result);
      }
      db.close();
    });
  });
};

module.exports = {
  ...connection
};
