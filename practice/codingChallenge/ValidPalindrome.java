package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Valid-Palindrome-68139eb2fc9a41c0ac0442b6dbdbc250
class Solution {

  // O(n), at most, check every character, O(1) space
  public boolean isPalindromeIterative(String s) {
    int left = 0;
    int right = s.length() - 1;
    while (left < right) {
      if (!Character.isLetterOrDigit(s.charAt(left))) {
        left += 1;
      }
      else if (!Character.isLetterOrDigit(s.charAt(right))) {
        right -= 1;
      }
      else {
        if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
          return false;
        }
      left += 1;
      right -= 1;
      }
    }
    return true;
  }

  // O(n) time, but also O(n) space
  public boolean isPalindromeRecursive(String s) {
    if (s.length() <= 1) {
      return true;
    }
    char left = s.charAt(0);
    char right = s.charAt(s.length() - 1);
    if (!Character.isLetterOrDigit(left)) {
      return isPalindromeRecursive(s.substring(1));
    }
    else if (!Character.isLetterOrDigit(right)) {
      return isPalindromeRecursive(s.substring(0, s.length() - 1));
    }
    else {
      if (Character.toLowerCase(left) != Character.toLowerCase(right)) {
          return false;
      }
      return isPalindromeRecursive(s.substring(1, s.length() - 1));
    }
  }
}
