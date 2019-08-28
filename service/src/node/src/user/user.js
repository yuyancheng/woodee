(function(exports, require, module, __filename, __dirname) {
  "use strict";

  var code = require("./../common/config"),
    db_loader = require("./../db/db-loader"),
    user = {
      name: "User API",
      desc: "",
      version: "1.0.0",
      author: "yyc",
      createdTime: "2016/08/12",
      lastUpdatedTime: "2016/08/12"
    };

  user.APIs = [
    { path: "/user/getInfo", type: ["get", "post"], fun: { ref: getInfo } },
    { path: "/user/logout", type: ["get", "post"], fun: { ref: logout } },
    { path: "/user/login", type: ["post"], fun: { ref: login, param: ["telephone", "password"] } },
    { path: "/user/login", type: ["post"], fun: { ref: login, param: ["telephone", "password"] } },
    { path: "/user/add", type: ["post", "get"], fun: { ref: addUser, param: ["name"] } },
    { path: "/user/register", type: ["get", "post"], fun: { ref: register, param: ["telephone", "name", "password"] } }
  ];

  user.init = function(exp) {
    var len = APIs.length;
    for (var i = 0; i < len; i++) {
      exp.get(APIs[i].path, APIs[i].fun);
      exp.post(APIs[i].path, APIs[i].fun);
    }
  };

  function login(req, res) {
    var param_names = [];
    console.log(123);

    if (!req.body.telephone && req.body.telephone != "0") {
      param_names.push("telephone");
    }

    if (!req.body.password && req.body.password != "0") {
      param_names.push("password");
    }

    if (
      (!req.body.telephone && req.body.telephone != "0") ||
      (!req.body.password && req.body.password != "0")
    ) {
      res.send({
        msg: "缺少必要参数：" + param_names.toString(),
        code: code.ERROR
      });
      return;
    }

    var filter = {
      telephone: req.body.telephone
    };

    db_loader.findOne("user", filter, function(data) {
      console.log("login.query: " + data);
      if (
        data &&
        data.password == req.body.password &&
        data.telephone == req.body.telephone
      ) {
        // 记录用户登录信息
        db_loader.insertOrUpdate(
          "login_token",
          {
            time_long: new Date().getTime() + 1000 * 60 * 60
          },
          { telephone: req.body.telephone },
          function(dt) {
            if (dt && dt.result && dt.result.ok) {
              res.send({
                code: code.OK,
                //token: dt.insertedId.id,
                msg: "Validate success!"
              });
            }
          }
        );
      } else {
        res.send({
          code: code.ERROR,
          msg: "User or Password is not right!"
        });
      }
    });
  }

  // getInfo
  function getInfo(req, res) {
    let filter = " WHERE ";

    for (var k in req.query) {
      filter = `${filter}${k}=${req.query[k]}`;
    }

    db_loader.findOne("users", filter, function(data) {
      if (data) {
        res.send(data);
      }
    });
  }

  // 退出
  function logout(req, res) {
    var filter = {};

    for (var k in req) {
      filter[k] = req[k];
    }

    db_loader.findMany("users", filter, function(data) {
      if (data) {
        res.send(data);
      }
    });
  }

  // 添加用户
  function addUser(req, res) {
    var user_info = [];
    var param_names = [];
    var filter = {};

    if (!req.query.name && req.query.name != "0") {
      param_names.push("name");
    } else {
      user_info.push(req.query.name);
    }
    if (!req.query.telephone && req.query.telephone != "0") {
        param_names.push("telephone");
    } else {
        user_info.push(req.query.telephone);
    }
    if (!req.query.create_time && req.query.create_time != "0") {
        param_names.push("create_time");
    } else {
        user_info.push(req.query.create_time);
    }

    if (!req.query.name || !req.query.telephone) {
      res.send({
        msg: "缺少必要参数：" + param_names.toString(),
        code: code.ERROR
      });
      return;
    }

    var filter = ` WHERE telephone=${req.query['telephone']} `;

    // 查询账号是否已注册
    db_loader.findOne("users", filter, function(data) {
      // 查账号已注册
      if (data) {
        res.send({
          code: code.EXIST,
          msg: "This user is existent, just check in, please!"
        });
      } else {
        // 添加用户信息
        db_loader.insertOne("users", user_info, 'id, name, telephone, create_time', function(data) {
          if (data && data.ok) {
            res.send({
              code: code.OK,
              msg: "Register successfully!"
            });
          }
        });
      }
    });
  }

  // 注册
  function register(req, res) {
    var user_info = {};
    var param_names = [];
    var filter = {};

    if (!req.body.name && req.body.name != "0") {
      param_names.push("name");
    } else {
      user_info.name = req.body.name;
    }
    if (!req.body.telephone && req.body.telephone != "0") {
      param_names.push("telephone");
    } else {
      user_info.telephone = req.body.telephone;
    }
    if (!req.body.password && req.body.password != "0") {
      param_names.push("password");
    } else {
      user_info.password = req.body.password;
    }
    if (!req.body.name || !req.body.telephone || !req.body.password) {
      res.send({
        msg: "缺少必要参数：" + param_names.toString(),
        code: code.ERROR
      });
      return;
    }

    var filter = {
      telephone: req.body.telephone
    };

    // 查询账号是否已注册
    db_loader.findOne("user", filter, function(data) {
      // 查账号已注册
      if (data) {
        res.send({
          code: code.EXIST,
          msg: "This user is existent, just check in, please!"
        });
      } else {
        // 添加用户信息
        db_loader.insertOne("user", user_info, null, function(data) {
          if (data && data.ok) {
            res.send({
              code: code.OK,
              msg: "Register successfully!"
            });
          }
        });
      }
    });
  }

  module.exports = user;
  module.exports.login = login;
  module.exports.logout = logout;
  module.exports.register = register;
})(exports, require, module);
