package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Implement-Stack-using-Queue-9bad9d1a93d54d04913fd95450a8a8c7
class MyStack2Queue {

  Queue<Integer> queue1;
  Queue<Integer> queue2;

  public MyStack() {
    queue1 = new LinkedList<Integer>();
    queue2 = new LinkedList<Integer>();
  }

  // O(n), iterate through entire "stack" per push
  public void push(int x) {
    queue2.offer(x);
    while (queue1.peek() != null) {
      queue2.offer(queue1.poll());
    }
    Queue<Integer> temp = queue1;
    queue1 = queue2;
    queue2 = temp;
  }

  // O(1), simple get
  public int pop() {
    return queue1.poll();
  }

  // O(1), simple peek
  public int top() {
      return queue1.peek();
  }

  // O(1), check if size == 0
  public boolean empty() {
      return queue1.isEmpty();
  }
}

class MyStack1Queue {

  Queue<Integer> queue;

  public MyStack() {
    queue = new LinkedList<Integer>();
  }

  // O(n), iterate through entire "stack" per push
  public void push(int x) {
    queue.offer(x);
    for (int i = queue.size(); i >= 0; i--) {
      queue.offer(queue.poll());
    }
  }

  // O(1), simple get
  public int pop() {
    return queue.poll();
  }

  // // O(1), simple peek
  public int top() {
    return queue.peek();
  }

  // // O(1), check if size == 0
  public boolean empty() {
    return queue.isEmpty();
  }
}
