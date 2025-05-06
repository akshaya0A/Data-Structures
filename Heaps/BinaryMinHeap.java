
// Akshaya Akkugari
// CSE 332
// EX02: Heaps
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryMinHeap<T extends Comparable<T>> implements MyPriorityQueue<T> {
    private int size; // Maintains the size of the data structure
    private T[] arr; // The array containing all items in the data structure
                     // index 0 must be utilized
    private Map<T, Integer> itemToIndex; // Keeps track of which index of arr holds each item.

    public BinaryMinHeap() {
        // This line just creates an array of type T. We're doing it this way just
        // because of weird java generics stuff (that I frankly don't totally
        // understand)
        // If you want to create a new array anywhere else (e.g. to resize) then
        // You should mimic this line. The second argument is the size of the new array.
        arr = (T[]) Array.newInstance(Comparable.class, 10);
        size = 0;
        itemToIndex = new HashMap<>();
    }

    // move the item at index i "rootward" until
    // the heap property holds
    private void percolateUp(int i) {
        int parent = (i - 1) / 2;
        while (i > 0 && arr[i].compareTo(arr[parent]) < 0) {
            swap(i, parent);
            i = parent;
            parent = (i - 1) / 2;
        }
    }


    // move the item at index i "leafward" until
    // the heap property holds
    // Check the left side, right side to move down
    private void percolateDown(int i) {
        // Track acts like the parent node
        int track = i;
        // While loop continnues till the track(parent) node isn't bigger than the left and right 
        while (true) {
            int left = (i*2) + 1;
            int right =(i*2) + 2;
            // check if the left side value for the tracking index is less than the tracking value
            // Check left side first cause if track acts like parent 
            // than the left side would be the next closest value
            if (left < size && arr[left].compareTo(arr[track]) < 0) {
                track = left;
            }
            // Now check if we can move even further down by comparing with right value
            if (right < size && arr[right].compareTo(arr[track]) < 0) {
                track = right;
            }
            // If the track index has changed than swap the places
            // Now there is a new parent node for us to go through
            if (track != i) {
                swap(i, track);
                i = track;
            } else {
                break;
            }
        }
    }

    // copy all items into a larger array to make more room.
    private void resize() {
        T[] larger = (T[]) Array.newInstance(Comparable.class, arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            larger[i] = arr[i];
        }
        arr = larger;
    }

    public void insert(T item) {
        if (size == arr.length) {
            resize();
        }
        // Insert element at the end of the array(end of heap)
        arr[size] = item;
        itemToIndex.put(item, size);
        // System.out.println("Inserting item: " + item);
        size++;
        updatePriority(size - 1);
        // System.out.println("Heap after insert: " + toString());
    }

    // This method removes and returns the element with the lowest value
    public T extract() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return remove(0);
    }

    // Remove the item at the given index.
    // Make sure to maintain the heap property!
    private T remove(int index) {
        if ((index >= size) || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        T element = rem(index);
        // Ensure the index is less than the size to
        // make sure the index we are llooking at is in the heap
        if (index < size) {
            updatePriority(index);
        }
        return element;
    }

    // Helper method for removing a value
    // Returns a T item
    private T rem(int index){
        // Get the element at the index and swap it
        // with the last element in the heap and set that to null to remove it
        T element = arr[index];
        swap(index, size - 1);
        arr[size - 1] = null;
        itemToIndex.remove(element);
        size--;
        return element;
    } 

    // Helper Method to swap elements
    private void swap(int id1, int id2) {
        T temp = arr[id1];
        arr[id1] = arr[id2];
        arr[id2] = temp;
        // THe arr[id] now holds the swapped element so its the id to id
        itemToIndex.put(arr[id1], id1);
        itemToIndex.put(arr[id2], id2);

        }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public void remove(T item) {
        remove(itemToIndex.get(item));
    }

    // Determine whether to percolate up/down
    // the item at the given index, then do it!
    private void updatePriority(int index) {
        int parent = (index - 1) / 2;
        // Checks if the value at the index is less than the parent nodes value 
        if (index > 0 && (arr[index].compareTo(arr[parent]) < 0)) {
            percolateUp(index);
        } else {
            percolateDown(index);
        }
        
    }

    // This method gets called after the client has
    // changed an item in a way that may change its
    // priority. In this case, the client should call
    // updatePriority on that changed item so that
    // the heap can restore the heap property.
    // Throws an IllegalArgumentException if the given
    // item is not an element of the priority queue.
    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public void updatePriority(T item) {
        if (!itemToIndex.containsKey(item)) {
            throw new IllegalArgumentException("Given item is not present in the priority queue!");
        }
        updatePriority(itemToIndex.get(item));
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public boolean isEmpty() {
        return size == 0;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public int size() {
        return size;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        return arr[0];
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public List<T> toList() {
        List<T> copy = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            copy.add(i, arr[i]);
        }
        return copy;
    }

    // For debugging
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        String str = "[(" + arr[0] + " " + itemToIndex.get(arr[0]) + ")";
        for (int i = 1; i < size; i++) {
            str += ",(" + arr[i] + " " + itemToIndex.get(arr[i]) + ")";
        }
        return str + "]";
    }

}
