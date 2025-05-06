
// Akshaya Akkugari
// CSE 332
// EX02: Heaps
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopKHeap<T extends Comparable<T>> {
    private BinaryMinHeap<T> topK; // Holds the top k items
    private BinaryMaxHeap<T> rest; // Holds all items other than the top k
    private int size; // Maintains the size of the data structure
    private final int k; // The value of k
    private Map<T, MyPriorityQueue<T>> itemToHeap; // Keeps track of which heap contains each item.

    // Creates a topKHeap for the given choice of k.
    public TopKHeap(int k) {
        topK = new BinaryMinHeap<>();
        rest = new BinaryMaxHeap<>();
        size = 0;
        this.k = k;
        itemToHeap = new HashMap<>();
    }

    // Returns a list containing exactly the
    // largest k items. The list is not necessarily
    // sorted. If the size is less than or equal to
    // k then the list will contain all items.
    // The running time of this method should be O(k).
    public List<T> topK() {
        return topK.toList();
    }

    // Add the given item into the data structure.
    // The running time of this method should be O(log(n)+log(k)).
    // Throws IllegalArgumentException() if item already in heap
    public void insert(T item) {
        if (itemToHeap.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        if (topK.size() < k) {
            topK.insert(item);
            itemToHeap.put(item, topK);
        } else {
            T min = topK.peek();
            if (item.compareTo(min) > 0) {
                T val = topK.extract();
                rest.insert(val);
                itemToHeap.put(val, rest);
                topK.insert(item);
                itemToHeap.put(item, topK);
            } else {
                rest.insert(item);
                itemToHeap.put(item, rest);
            }
        }
        size++;
    }

    // Indicates whether the given item is among the
    // top k items. Should return false if the item
    // is not present in the data structure at all.
    // The running time of this method should be O(1).
    // We have provided a suggested implementation,
    // but you're welcome to do something different!
    public boolean isTopK(T item) {
        return itemToHeap.get(item) == topK;
    }

    // To be used whenever an item's priority has changed.
    // The input is a reference to the items whose priority
    // has changed. This operation will then rearrange
    // the items in the data structure to ensure it
    // operates correctly.
    // Throws an IllegalArgumentException if the item is
    // not a member of the heap.
    // The running time of this method should be O(log(n)+log(k)).
    public void updatePriority(T item) {
        if (!itemToHeap.containsKey(item)) {
            throw new IllegalArgumentException("Item not in the priority queue!");
        }
        MyPriorityQueue<T> heap = itemToHeap.get(item);
        heap.updatePriority(item);
        // Make sure to remove the item because it may need to move to a different
        // heap(reposition the item).
        heap.remove(item);
        itemToHeap.remove(item);
        size--;
        insert(item);

        // Check if topK is less than the desired size and if rest is non empty
        if (topK.size() < k && rest.size() > 0) {
            // If topK doesn’t have enough items, find the best item in rest and move it to
            // topK.
            T val = null;
            // Get the largest element from the rest heap
            val = rest.peek();
            if (val != null) {
                rest.remove(val);
                topK.insert(val);
                itemToHeap.put(val, topK);
            }
        }

    }

    // Removes the given item from the data structure
    // throws an IllegalArgumentException if the item
    // is not present.
    // The running time of this method should be O(log(n)+log(k)).
    public void remove(T item) {
        if (!itemToHeap.containsKey(item)) {
            throw new IllegalArgumentException("Item not in the priority queue!");
        }
        // find which heap the item is in
        MyPriorityQueue<T> heap = itemToHeap.get(item);
        heap.remove(item);
        itemToHeap.remove(item);
        size--;
        // If topK size is less than the desired size and rest isn't empty
        // than we take the largest element from rest to topK
        if (((topK.size() < k) && !(rest.isEmpty()))) {
            T val = rest.extract();
            transfer(val);
        }
    }

    // Helper method to swap from rest to topK
    private void transfer(T val) {
        topK.insert(val);
        itemToHeap.put(val, topK);
    }

}

