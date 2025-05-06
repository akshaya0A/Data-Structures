public class AVLTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {
    public AVLTree() {
        super();
    }

    // Returns the old value associated with the key, or null if it's a new key.
    public V insert(K key, V value) {
        if (root == null) {
            root = new TreeNode<>(key, value);
            root.updateHeight();
            size++;
            return null;
        }
        // Check if the key already exisits
        V answer = find(key);
        if (answer == null) {
            // Since key not found need to add a new node so increment size
            size++;
        }
        // Call recusive method to put in new key
        root = insert(key, value, root);
        return answer;
    }

    // Helper method to recursively insert the treeNode
    // Similar to how insert in BST works
    // Returns the old value associated with the key, or null if it's a new key.
    private TreeNode<K, V> insert(K key, V value, TreeNode<K, V> curr) {
        // If empty tree need to add a new treeNode
        if (curr == null) {
            return new TreeNode<>(key, value);
        }
        // Check which branch should the treeNode should be placed
        if (curr.key.compareTo(key) > 0) {
            curr.left = insert(key, value, curr.left);
        } else if (curr.key.compareTo(key) < 0) {
            curr.right = insert(key, value, curr.right);
        } else {
            // The key already exists so update the value
            curr.value = value;
            return curr;
        }
        // After adding the treeNode update height
        curr.updateHeight();
        // Balance the tree if needed
        return rotation(diff(curr), curr);
    }

    // Helper method to rotate the tree to ensure it's rebalenced
    // Returns the new root of the subtree after rotation
    private TreeNode<K, V> rotation(int difference, TreeNode<K, V> curr) {
        // Rotate left case
        if (difference > 1) {
            // Rotate right left case
            if (diff(curr.right) < 0) {
                curr.right = rotateRight(curr.right);
            }
            return rotateLeft(curr);
        }
        // Rotate right case
        if (difference < -1) {
            // Rotate left right case
            if (diff(curr.left) > 0) {
                curr.left = rotateLeft(curr.left);
            }
            return rotateRight(curr);
        }
        return curr;
    }

    // Private helper method to get the difference in heights(balence factor)
    // Return an integr for the balence factor
    private int diff(TreeNode<K, V> curr) {
        int rightHeight = 0;
        int leftHeight = 0;
        if (curr.right == null) {
            rightHeight = -1;
        } else {
            rightHeight = curr.right.height;
        }
        if (curr.left == null) {
            leftHeight = -1;
        } else {
            leftHeight = curr.left.height;
        }
        return rightHeight - leftHeight;
    }

    // Rotates the tree with a right rotation
    // The new root of the subtree after right rotation
    private TreeNode<K, V> rotateRight(TreeNode<K, V> curr) {
        TreeNode<K, V> node = curr.left;
        curr.left = node.right;
        node.right = curr;
        // Ensure height maintained for root node and lower subtree
        curr.updateHeight();
        node.updateHeight();
        return node;
    }

    // Rotates the tree with a left rotation
    // The new root of the subtree after left rotation
    private TreeNode<K, V> rotateLeft(TreeNode<K, V> curr) {
        TreeNode<K, V> node = curr.right;
        curr.right = node.left;
        node.left = curr;
        // Ensure height maintained for root node and lower subtree
        curr.updateHeight();
        node.updateHeight();
        return node;
    }
}
