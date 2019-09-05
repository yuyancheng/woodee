function throttle(func, wait) {
    let oldValue = wait;
    if (!func.num) {
        func.num = 1;
    }
    let timeout;
    return function() {
        let context = this;
        let args = arguments;
        if (func.num == 1) {
            func.num ++;
            wait = 0;
        } else {
            wait = oldValue;
        };
        if (!timeout) {
            timeout = setTimeout(() => {
                timeout = null;
                func.apply(context, args)
            }, wait)
        }

    }
};
let i = 1;
const fn = throttle(function () {
    console.log(i++);
}, 3000);
setInterval(fn, 10);