//Akshaya Akkugari 
// CSE 332
// Robbie Webber
// 26 SP

// A generic queue implementation from a circular array follwoing FIFO.
public class ArrayQueue<T> implements MyQueue<T> {
    private T[] arr;
    private int front;
    private int back;
    private int size;

    // Constructor to initialize the queue with default capacity of 10
    public ArrayQueue() {
        arr = (T[]) new Object[10];
        front = 0;
        back = 0;
        size = 0;
    }

    // Adds an item into the queue.
    public void enqueue(T item) {
        // If the array is full, resize it to double the current size
        if (size() == arr.length) {
            T[] copy = (T[]) new Object[2 * size()];
            // Copy elements in correct order starting from front
            for (int i = 0; i < size(); i++) {
                copy[i] = arr[(front + i) % size];
            }
            // Reset front and back for new array
            front = 0;
            back = size;
            arr = copy;
        }
        arr[back] = item;
        // Circularly move the front index and wrapp around to the beginning if
        // necessary
        back = (back + 1) % arr.length;
        size++;
    }

    // Removes and returns the least-recently added item from the queue
    // Throws an IllegalStateException if the queue is empty
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Queue");
        } else {
            T item = arr[front];
            // Circularly move the front index and wrapp around to the beginning if
            // necessary
            front = (front + 1) % arr.length;
            size--;
            return item;
        }
    }

    // Returns the least-recently added item from the queue
    // Throws an IllegalStateException if the queue is empty
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Queue");
        } else {
            return arr[front];
        }
    }

    // Return the number of items currently in the queue
    public int size() {
        return size;
    }

    // Returns a boolean to indicate whether the queue has items
    public boolean isEmpty() {
        return size == 0;
    }
}
