//Akshaya Akkugari 
// CSE 332
// Robbie Webber
// 26 SP

import java.text.Normalizer;

// This class implements a generic stack using a linked list following LIFO. 
public class LinkedStack<T> implements MyStack<T> {

    private static class ListNode<T> {
        private final T data;
        private ListNode<T> next;

        private ListNode(T data, ListNode<T> next) {
            this.data = data;
            this.next = next;
        }

        private ListNode(T data) {
            this.data = data;
        }
    }

    private ListNode<T> front;
    private int size;

    // Constructor to initialize an empty stack
    public LinkedStack() {
        front = null;
        size = 0;
    }

    // Adds an item into the stack
    // Adds item to the front of the linked list
    public void push(T item) {
        front = new ListNode<>(item, front);
        size++;
    }

    // Removes and returns the most recently added item from the stack
    // removes the front element from the linked list
    // throws an IllegalStateException if the stack is empty
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Stack");
        } else {
            T rem = front.data;
            front = front.next;
            size--;
            return rem;
        }
    }

    // Returns the most recently added item in the stack
    // Returns the front element from the linked list
    // throws an IllegalStateException if the stack is empty
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Stack");
        } else {
            return front.data;
        }
    }

    // Returns the number of items in the stack
    public int size() {
        return size;
    }

    // Returns a boolean indicating whether the stack has items
    public boolean isEmpty() {
        return front == null;

    }

}
