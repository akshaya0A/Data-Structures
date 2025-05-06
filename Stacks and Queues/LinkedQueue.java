//Akshaya Akkugari 
// CSE 332
// Robbie Webber
// 26 SP

// This class implements a generic queue using a linked list following FIFO. 
public class LinkedQueue<E> implements MyQueue<E> {

    private static class ListNode<E> {
        private final E data;
        private ListNode<E> next;

        private ListNode(E data, ListNode<E> next) {
            this.data = data;
            this.next = next;
        }

        private ListNode(E data) {
            this.data = data;
            this.next = null;
        }

    }

    private ListNode<E> front;
    private ListNode<E> back;
    private int size;

    // Constructor to initialize the an empty queue
    public LinkedQueue() {
        front = null;
        back = null;
        size = 0;
    }

    // Adds an item into the queue
    // Adds item to the end of the linkedList
    public void enqueue(E item) {
        ListNode<E> newNode = new ListNode<>(item);
        if (isEmpty()) {
            front = newNode;
            back = newNode;
        } else {
            back.next = newNode;
            back = back.next;
        }
        size++;
    }

    // Removes and returns the least-recently added item from the queue
    // Removes the first elements from the Linked List
    // Throws an IllegalStateException if the queue is empty
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Queue");
        } else {
            E rem = front.data;
            front = front.next;
            size--;
            return rem;
        }
    }

    // Returns the least-recently added item from the queue
    // Returns the first elemet from the linked list
    // Throws an IllegalStateException if the queue is empty
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Queue");
        } else {
            return front.data;
        }
    }

    // Return the number of items currently in the queue
    public int size() {
        return size;
    }

    // Returns a boolean to indicate whether the queue has items
    public boolean isEmpty() {
        return front == null;
    }
}
