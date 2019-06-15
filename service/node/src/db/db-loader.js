"use strict";

const dbLoader = {};
let connection = {};
let loader = require("./mysql");

connection = { ...loader };

const setDB = dbType => {
  if (dbType) {
    if (dbType === "mongoDB") {
      loader = require("./mongo");
      return { ...loader };
    } else if (dbType === "mysql") {
      loader = require("./mysql");
      return { ...loader };
    } else {
      console.error("没有找到指定的数据库类型！");
    }
  } else {
    console.error("请指定要连接的数据库类型！");
  }
};

module.exports = {
  setDB,
  ...connection
};
