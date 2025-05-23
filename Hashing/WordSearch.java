import java.util.ArrayList;
import java.util.List;

//  WordSearch performs a search for predefined words within a grid of elements.
// It checks all 8 directions from every point in the grid for all word lengths ≥ 2.
// The words found are tracked in a hash table for constant-time access.
public class WordSearch<T> {
    T[][] grid;
    DeletelessDictionary<Word<T>, Boolean> seen; // maps words to booleans to indicate if they've been found
    String[] directions = { "U", "D", "L", "R", "UL", "UR", "DL", "DR" };

    // initializes the grid and inserts all words from the dictionary
    //  into the hash table as unseen (false). Then runs the word search.
    public WordSearch(T[][] grid, List<Word<T>> dictionary) {
        this.seen = new ChainingHashTable<>();
        this.grid = grid;
        for (Word<T> word : dictionary) {
            seen.insert(word, false);
        }
        wordSearch();
    }

    // if the given word is valid then mark it as seen in the hash table.
    private void addIfWord(Word<T> word) {
        if (valid(word)) {
            seen.insert(word, true);
        }
    }

    private boolean valid(Word<T> word) {
        return seen.contains(word);
    }

    // returns the number of valid words found in the grid.
    public int countWords() {
        int count = 0;
        List<Boolean> vals = seen.getValues();
        for (boolean val : vals) {
            if (val) {
                count++;
            }
        }
        return count;
    }

    // returns the list of valid words found in the grid.
    public List<Word<T>> getWords() {
        List<Word<T>> words = new ArrayList<>();
        List<Boolean> vals = seen.getValues();
        List<Word<T>> keys = seen.getKeys();
        for (int i = 0; i < vals.size(); i++) {
            if (vals.get(i)) {
                words.add(keys.get(i));
            }
        }
        return words;
    }

    // Performs the word search.
    // Iterates through every row/column pair as the start of a word.
    // Then checks in all 8 directions for every valid word length.
    // If we consider the input size to be the width*height of the grid
    // then this algorithm has a quadratic running time.
    private void wordSearch() {
        int width = grid[0].length;
        int height = grid.length;
        for (int x = 0; x < width; x++) { // for each column
            for (int y = 0; y < height; y++) { // for each row
                for (int n = 2; n <= Math.max(width, height); n++) { // for each length
                    for (String dir : directions) { // for each direction
                        if (inBounds(n, x, y, dir)) { // check that start and end indices are in-bounds
                            Word<T> word = new Word<>(grid, x, y, dir, n); // make the word
                            addIfWord(word); // if the word object is in the dictionary, mark it as seen.
                        }
                    }
                }
            }
        }
    }

    // For debugging purposes. Prints out all the words
    // from the dictionary that were found.
    public void printFoundWords() {
        for (Word<T> word : this.getWords()) {
            System.out.println(word);
        }
    }

    // private helper method to perform bounds checking while exploring the grid.
    private boolean inBounds(int n, int x, int y, String direction) {
        int width = grid[0].length;
        int height = grid.length;
        boolean valid = x <= width && y <= height;
        if (direction.charAt(0) == 'U') {
            valid = valid && (n <= y + 1);
        }
        if (direction.charAt(0) == 'D') {
            valid = valid && (y + n <= width);
        }
        if (direction.charAt(direction.length() - 1) == 'L') {
            valid = valid && (n <= x + 1);
        }
        if (direction.charAt(direction.length() - 1) == 'R') {
            valid = valid && (x + n <= height);
        }
        return valid;
    }
}
