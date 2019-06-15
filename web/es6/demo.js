function countMoney(total) {
  if (total < 1) {
    return 0;
  }
  var result = 0;
  var maxAmount = total / 1;
  for (var i = 0; i <= maxAmount; i++) {
    // 5
    for (var j = 0; j <= maxAmount; j++) {
      // 3
      for (var k = 0; k <= maxAmount; k++) {
        // 2
        var sum = i * 5 + j * 3 + k * 1;
        if (sum == total) {
          result++;
          break;
        } else if (sum > total) {
          break;
        }
      }
    }
  }
  return result;
}
// console.log(countMoney(10));

const getIndex =  (arr, v) => {
  if (typeof arr !== 'object' || arr.constructor !== Array) {
    return new Error('First param must be a Array!');
  }
  if (v.constructor === Function) {
    return v.call(null);
  } else {
    return arr.indexOf(v);
  }
};

const wordCounter = (str) => {
  const wordSets = {};
  const wordArr = [];
  const wordList = str.split(/[^a-zA-Z]+/);

  wordList.map((v) => {
    if (getIndex(wordArr, () => {
      let reg = null;
      for (i=0; i<wordArr.length; i++) {
        const regStr = `^${wordArr[i]}$`;
        reg = new RegExp(regStr, 'i');
        if (reg.test(v)) {
          return i;
        }
      }
      return -1;
    }) === -1 && /[a-zA-Z]+/.test(v)) {
      wordArr.push(v.toLowerCase());
      wordSets[v.toLowerCase()] = 1;
    } else if (/\S/.test(v)){
      wordSets[v.toLowerCase()] ++;
    }
  });
  return wordSets;
};

const wordStr = 'In my ./ dual profession as an educator and health care provider, I have worked with numerous children infected with the virus that causes AIDS. The relationships that I have had with these special kids have been gifts in my life. They have taught me so many things, but I have especially learned that great courage can be found in the smallest of packages. Let me tell you about Tyler.';
// console.log(wordCounter(wordStr))

const obj = {
  get fun() {
    console.log(222);
    return () => {
      console.log(111);
      return 333;
    };
  },
  name: 'js'
};

//const obj2 = Object.assign({}, obj);
//console.log(obj2.fun());


var desp1 = Object.getOwnPropertyDescriptor(obj);

// console.log(desp1, 'name');

var fl = true;

// console.log(Object.getPrototypeOf('false'))

// console.log(Object.fromEntries(new URLSearchParams('foo=bar&baz=qux')))


const http = require('http');
const hostname = '127.0.0.1';
const port = 3000;

const server = http.createServer((req, res) => {
  console.log(req.aborted);
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end('Hello, World!\n');
});

// server.listen(port, hostname, () => {
//   console.log(`Server running at http://${hostname}:${port}/`);
// });
// console.log(http.ClientRequest);
// console.log(http.METHODS);
// console.log(http.STATUS_CODES);

var s = 'aaa_aa_a';
var r1 = /a+/g;
var r2 = /a+/y;

r1.exec(s) // ["aaa"]
r2.exec(s) // ["aaa"]

console.log(r1.exec(s)) // ["aa"]
console.log(r2.exec(s)) // null