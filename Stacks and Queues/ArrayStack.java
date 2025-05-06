//Akshaya Akkugari 
// CSE 332
// Robbie Webber
// 26 SP

// A generic stack implementation using a resizing array by following LIFO.
public class ArrayStack<T> implements MyStack<T> {
    private T[] arr;
    private int size;

    // Constructor to initialize the stack with a default capacity of 10
    public ArrayStack() {
        arr = (T[]) new Object[10];
        size = 0;
    }

    // Adds an item into the stack
    public void push(T item) {
        if (size == arr.length) {
            // Create a new array with double the size
            T[] copy = (T[]) new Object[2 * arr.length];
            // Copy all elements from the old array to the new array
            for (int i = 0; i < size; i++) {
                copy[i] = arr[i];
            }
            arr = copy;
        }
        arr[size] = item;
        size++;
    }

    // Removes and returns the most recently added item from the stack
    // throws an IllegalStateException if the stack is empty
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Stack");
        }
        size--;
        return arr[size];
    }

    // Returns the most recently added item in the stack
    // throws an IllegalStateException if the stack is empty
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Empty Stack");
        }
        return arr[size - 1];
    }

    // Returns the number of items in the stack
    public int size() {
        return size;
    }

    // Returns a boolean indicating whether the stack has items
    public boolean isEmpty() {
        return size == 0;
    }
}
