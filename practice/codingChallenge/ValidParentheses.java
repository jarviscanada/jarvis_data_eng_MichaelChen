package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Valid-Parentheses-794adec3fe4a447a95c144949fadb302
class Solution {

  // O(n), one pass through string
  public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<Character>();

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '(' || c == '[' || c == '{') {
        stack.push(c);
      }
      else {
        if (stack.isEmpty()) {
          return false;
        }
        char next = stack.pop();
        if (c == ')') {
          if (next != '('){
            return false;
          }
        }
        else if (c == ']') {
          if (next != '['){
            return false;
          }
        }
        else if (c == '}') {
          if (next != '{'){
            return false;
          }
        }
      }
    }
    return stack.isEmpty();
  }
}
