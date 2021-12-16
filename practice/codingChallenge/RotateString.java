package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Rotate-String-600ae24475364a0c9b72e229add79ba9
class Solution {

  // O(nm), but n = m, so O(n^2) 
  public boolean rotateString(String s, String goal) {
    if (s.length() != goal.length()) {
      return false;
    }
    return (s + s).contains(goal);
  }
}
