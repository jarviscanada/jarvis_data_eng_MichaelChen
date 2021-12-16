package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Fibonacci-Number-Climbing-Stairs-77249ed06fc742cfbcf00ec7531e58a4
class Solution {

  // O(n^2), each call calls 2 more calls
  public int fibRecursion(int n) {
    if (n <= 1) {
      return n;
    }
      return fib(n - 1) + fib(n - 2);
  }

  // O(n), at most, 31 calls actually do math, the rest just refer to the memo
  int[] memo = new int[31];

  public int fibDP(int n) {
    if (n <= 1) {
      return n;
    }
    if (memo[n] == 0) {
      memo[n] = fib(n - 1) + fib(n - 2);
    }
    return memo[n];
  }
}
