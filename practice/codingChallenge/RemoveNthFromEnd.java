package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Nth-Node-From-End-of-LinkedList-e4a2e3c7a0294e838905c3087ca572a8
class Solution {

  // O(n), one pass through list with 2 pointers
  public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode first = head;
    ListNode second = head;
    for (; n > 0; n--) {
      first = first.next;
    }
    if (first == null) {
      return head.next;
    }
    while (first.next != null) {
      first = first.next;
      second = second.next;
    }
    second.next = second.next.next;
    return head;
  }
}
