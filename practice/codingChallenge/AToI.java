package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/String-to-Integer-atoi-56cd9885e8334d87a28e19f97a4fd3e2
class Solution {

  // O(n), at most one pass
  public int myAtoi(String s) {
    int sign = 1;
    char[] c = s.trim().toCharArray();
    if (c[0] == '-') {
      sign = -1;
      c[0] = '0';
    }
    else if (c[0] == '+') {
      c[0] = '0';
    }
    int res = 0;
    for (int i = 0; i < c.length; i++) {
      int curr = (int)c[i];
      // int('0') = 48, int('9') = 57
      if (curr < 48 || curr > 57) {
        break;
      }
      if (res > Integer.MAX_VALUE / 10) {
        if (sign == 1) {
          return Integer.MAX_VALUE;
        }
        else {
          return Integer.MIN_VALUE;
        }
      }
      res *= 10;
      res += curr - 48;
    }
    return res * sign;
  }
}
