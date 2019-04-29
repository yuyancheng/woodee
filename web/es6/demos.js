function* objectEntries(obj) {
    let propKeys = Reflect.ownKeys(obj);

    for (let propKey of propKeys) {
        yield [propKey, obj[propKey]];
    }
}

let jane = {
    first: 'Jane',
    last: 'Doe'
};

// for (let [key, value] of objectEntries(jane)) {
//     console.log(`${key}: ${value}`);
// }
// first: Jane
// last: Doe

/////////////////////////

// class A {
//     constructor(...args) {
//         this._query = args[0];

//     }
//     setval(v = 'hello') {
//         this.value = v;
//     }
// };

// class B extends A {
//     constructor(...args) {
//         super();
//     }
//     getval() {
//         return this.value;
//     }
// }
// const b = new B();
// b.setval();
// console.log(b.getval());

/////////////////////

async function timeout(ms) {
    return await new Promise((resolve) => {
        setTimeout(() => {
            resolve('hi');
        }, ms);
    });
}

async function asyncPrint(value, ms) {
    console.log(value);
    return await timeout(ms);
}

// asyncPrint('hello world', 2000).then(v => {
//     console.log(v)
// });

/////////////////////////////

async function fun() {
    return await 'hello';
}

const p = fun();
console.log(p);

