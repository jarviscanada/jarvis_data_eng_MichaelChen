package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Duplicate-LinkedList-Node-9c8c0d4207974d628d67789793aba743
class Solution {

  // O(n), one pass through list
  public void DuplicateNode(Node node) {

    Set<Integer> seen = new HashSet<>();
    Node ptr = node;
    while (ptr != null && ptr.next != null) {
      if (seen.contains(ptr.next.data)) {
        ptr.next = ptr.next.next;
      }
      else {
        seen.put(ptr.next.data)
      }
      ptr = ptr.next;
    }
  }
}
