package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/Implement-Queue-using-Stacks-99d831aa563a45778c2681763cdc44bd
class MyQueueLinear {

  Deque<Integer> stack1;
  Deque<Integer> stack2;

  public MyQueue() {
    stack1 = new ArrayDeque<Integer>();
    stack2 = new ArrayDeque<Integer>();
  }

  // O(n), iterating through whole "queue"
  public void push(int x) {
    while (!stack1.isEmpty()) {
      stack2.push(stack1.pop());
    }
    stack2.push(x);
    while (!stack2.isEmpty()) {
      stack1.push(stack2.pop());
    }
  }

  // O(1), stack always have reference to top/front
  public int pop() {
    return stack1.pop();
  }

  // // O(1), stack always have reference to top/front
  public int peek() {
    return stack1.peek();
  }

  // // O(1), stack always have reference to top/front
  public boolean empty() {
    return stack1.isEmpty();
  }
}

class MyQueueConstant {

  Deque<Integer> stack1;
  Deque<Integer> stack2;

  public MyQueue() {
    stack1 = new ArrayDeque<Integer>();
    stack2 = new ArrayDeque<Integer>();
  }

  // O(1), just push
  public void push(int x) {
    stack2.push(x);
  }

  // O(1), when stack1 not empty, O(n) when it is
  public int pop() {
    peek();
    return stack1.pop();
  }

  // O(1), when stack1 not empty, O(n) when it is
  public int peek() {
    if (stack1.isEmpty()) {
      while (!stack2.isEmpty()) {
        stack1.push(stack2.pop());
      }
    }
    return stack1.peek();
  }
  // O(1), check top/front
  public boolean empty() {
    return stack1.isEmpty();
  }
}
