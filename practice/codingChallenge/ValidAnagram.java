package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Valid-Anagram-7bf518f2e56e414ba18eadbc2a4b50c7
class Solution {

  // O(nlogn), average case for sorting
  public boolean isAnagramSort(String s, String t) {
      char[] sChars = s.toCharArray();
      char[] tChars = t.toCharArray();
      Arrays.sort(sChars);
      Arrays.sort(tChars);
      return Arrays.equals(sChars, tChars);
  }

  // O(n), one pass through each string
  public boolean isAnagramMap(String s, String t) {
    if (s.length() != t.length()) {
      return false;
    }
    HashMap<Character, Integer> dict = new HashMap<>();
    for (int i = 0; i < s.length(); i++) {
      char letter = s.charAt(i);
      if (dict.containsKey(letter)) {
        dict.put(letter, dict.get(letter) + 1);
      }
      else {
        dict.put(letter, 1);
      }
    }
    for (int i = 0; i < t.length(); i++) {
      char letter = t.charAt(i);
      if (dict.containsKey(letter)) {
        dict.put(letter, dict.get(letter) - 1);
        if (dict.get(letter) < 1) {
          dict.remove(letter);
        }
      }
      else {
        return false;
      }
    }
    return true;
  }
}
