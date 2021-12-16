package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Two-Sum-ee412ff33a544965a10d22568e2fa64f
class Solution {

  // O(n^2), nested loop
  public int[] twoSum2Loop(int[] nums, int target) {
      for (int i = 0; i < nums.length - 1; i++) {
        for (int j = i + 1; j < nums.length; j++) {
          if (nums[i] + nums[j] == target) {
            return new int[] {i, j};
          }
        }
      }
    return null;
  }

  // O(n), one pass
  public int[] twoSum1Pass(int[] nums, int target) {
      HashMap<Integer, Integer> seen = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
          int diff = target - nums[i];
          if (seen.containsKey(diff)) {
            return new int[] {i, seen.get(diff)};
          }
          seen.put(nums[i], i);
        }
      return null;
  }
}
