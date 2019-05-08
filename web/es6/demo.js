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
console.log(countMoney(10));
