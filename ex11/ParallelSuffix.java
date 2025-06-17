import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ParallelSuffix {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    public static int[] suffixSum(int[] arr, int cutoff) {
        ParallelSuffix.CUTOFF = cutoff;
        Node root = POOL.invoke(new BuildTreeTask(arr, 0, arr.length));
        int[] suffixSum = new int[arr.length];
        POOL.invoke(new PopulateSuffixSum(arr, suffixSum, root, 0));
        return suffixSum;
    }

    private static class PopulateSuffixSum extends RecursiveAction {
        private int[] arr;
        private int[] output;
        private Node root;
        private int sumAllRight;

        public PopulateSuffixSum(int[] arr, int[] output, Node root, int sumAllRight) {
            this.arr = arr;
            this.output = output;
            this.root = root;
            this.sumAllRight = sumAllRight;
        }

        public void compute() {
            // For the base case, the node's two children will be null
            // For the recursive case, the node's two children will be non-null
            // and you will need to set the left sum for each child node
            if (root.leftChildNode == null && root.rightChildNode == null) {
                // sum of all elements to the right
                int total = sumAllRight;
                // Fill suffix sums from right to left
                for (int i = root.hi - 1; i >= root.lo; i--) {
                    total += arr[i];
                    output[i] = total;
                }
            } else {
                PopulateSuffixSum right = new PopulateSuffixSum(arr, output, root.rightChildNode, sumAllRight);
                PopulateSuffixSum left = new PopulateSuffixSum(arr, output, root.leftChildNode,
                        sumAllRight + root.rightChildNode.sum);
                left.fork();
                right.compute();
                left.join();
            }
        }
    }

    private static class BuildTreeTask extends RecursiveTask<Node> {
        private int[] arr;
        private int lo;
        private int hi;

        public BuildTreeTask(int[] arr, int lo, int hi) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
        }

        public Node compute() {
            if (hi - lo <= ParallelSuffix.CUTOFF) {
                int sum = 0;
                for (int i = lo; i < hi; i++) {
                    sum += arr[i];
                }
                // leaf node
                return new Node(lo, hi, sum, null, null);
            } else {
                // 1) create a new node for the parent
                // 2) create a new task for the left and right children, compute each of them
                // 3) set the parent node's sum to be the sum of the left and right children
                // 4) return the parent node
                int mid = (hi + lo) / 2;
                BuildTreeTask leftT = new BuildTreeTask(arr, lo, mid);
                BuildTreeTask rightT = new BuildTreeTask(arr, mid, hi);
                leftT.fork();
                Node right = rightT.compute();
                Node left = leftT.join();
                return new Node(lo, hi, left.sum + right.sum, left, right);

            }
        }
    }
}
