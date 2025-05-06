import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainingHashTable<K, V> implements DeletelessDictionary<K, V> {
    private List<Item<K, V>>[] table; // the table itself is an array of linked lists of items.
    private int size;
    private static int[] primes = { 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853 };
    private int k = 15;

    public ChainingHashTable() {
        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, primes[0]);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // TODO
    public V insert(K key, V value) {
        // load fsactor part
        // double load_factor = (size * (1.0) / table.length);
        if (this.size >= (2 * table.length)) {
            hash();
        }
        int k = Math.floorMod(key.hashCode(), table.length);
        for (Item<K, V> item : table[k]) {
            if (item.key.equals(key)) {
                V oldVal = item.value;
                item.value = value;
                return oldVal;

            }
        }
        table[k].add(new Item<>(key, value));
        size++;
        return null;
    }

    private void hash() {
        int j = 0;
        while (j < primes.length - 1 && primes[j] <= table.length * 2) {
            j++;
        }
        int resize = 0;
        if (j >= primes.length) {
            resize = (int) Math.pow(2, k) + 1;
            k++;
        } else {
            resize = primes[j];
        }
        List<Item<K, V>>[] oldTable = table;
        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, resize);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
        for (List<Item<K, V>> bucket : oldTable) {
            for (Item<K, V> item : bucket) {
                insert(item.key, item.value);
            }
        }
    }

    public V find(K key) {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        int i = Math.floorMod(key.hashCode(), table.length);
        if (!contains(key)) {
            return null;
        }
        for (Item<K, V> item : table[i]) {
            if (item.key.equals(key)) {
                return item.value;
            }
        }
        return null;
    }

    public boolean contains(K key) {
        int i = Math.floorMod(key.hashCode(), table.length);
        for (Item<K, V> item : table[i]) {
            if (item.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    // TODO
    public List<K> getKeys() {
        ArrayList<K> keys = new ArrayList<>();
        for (List<Item<K, V>> i : table) {
            for (Item<K, V> item : i) {
                keys.add(item.key);
            }
        }
        return keys;
    }

    // TODO
    public List<V> getValues() {
        ArrayList<V> vals = new ArrayList<>();
        for (List<Item<K, V>> i : table) {
            for (Item<K, V> item : i) {
                vals.add(item.value);
            }
        }
        return vals;
    }

    public String toString() {
        String s = "{";
        s += table[0];
        for (int i = 1; i < table.length; i++) {
            s += "," + table[i];
        }
        return s + "}";
    }

}
