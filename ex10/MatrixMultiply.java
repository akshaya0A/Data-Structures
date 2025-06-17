import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MatrixMultiply {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    // Behavior should match Sequential.multiply.
    // Ignoring the initialization of arrays, your implementation should have n^3
    // work and log(n) span
    public static int[][] multiply(int[][] a, int[][] b, int cutoff) {
        MatrixMultiply.CUTOFF = cutoff;
        int[][] product = new int[a.length][b[0].length];
        POOL.invoke(new MatrixMultiplyAction(a, b, product, 0, a.length, 0, b[0].length));// TODO: add parameters to
                                                                                          // match your constructor
        return product;
    }

    // Behavior should match the 2d version of Sequential.dotProduct.
    // Your implementation must have linear work and log(n) span
    public static int dotProduct(int[][] a, int[][] b, int row, int col, int cutoff) {
        MatrixMultiply.CUTOFF = cutoff;
        return POOL.invoke(new DotProductTask(a, b, row, col, 0, a[0].length)); // TODO: add parameters to match your
                                                                                // constructor
    }

    private static class MatrixMultiplyAction extends RecursiveAction {
        // TODO: select fields
        private final int[][] a, b, product;
        private final int left, right, top, bottom;

        public MatrixMultiplyAction(int[][] a, int[][] b, int[][] product, int left, int right, int top, int bottom) {
            // TODO: implement constructor
            this.a = a;
            this.b = b;
            this.product = product;
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;

        }

        public void compute() {
            int rows = right - left;
            int cols = top - bottom;
            // Base case
            // find dot product for cell in the product matrix
            if (rows * cols <= CUTOFF) {
                for (int i = left; i < right; i++) {
                    for (int j = top; j < bottom; j++) {
                        product[i][j] = dotProduct(a, b, i, j, CUTOFF);
                    }
                }
            } else if (rows >= cols) {
                // split by rows so vertically
                int mid = (left + right) / 2;
                MatrixMultiplyAction leftTask = new MatrixMultiplyAction(a, b, product, left, mid, top, bottom);
                MatrixMultiplyAction rightTask = new MatrixMultiplyAction(a, b, product, mid, right, top, bottom);
                leftTask.fork();
                rightTask.compute();
                leftTask.join();
            } else {
                // split by cols so horizontally
                int mid = (top + bottom) / 2;
                MatrixMultiplyAction topTask = new MatrixMultiplyAction(a, b, product, left, right, top, mid);
                MatrixMultiplyAction bottomTask = new MatrixMultiplyAction(a, b, product, left, right, mid, bottom);
                topTask.fork();
                bottomTask.compute();
                topTask.join();
            }
        }

    }

    // helper mehtod to compute the dot product of one row and column
    private static class DotProductTask extends RecursiveTask<Integer> {
        private final int[][] a, b;
        private final int row, col;
        private final int lo, hi;

        public DotProductTask(int[][] a, int[][] b, int row, int col, int lo, int hi) {
            this.a = a;
            this.b = b;
            this.row = row;
            this.col = col;
            this.lo = lo;
            this.hi = hi;
        }

        public Integer compute() {
            // base case computing dotproduct over the range lo to hi
            if (hi - lo <= CUTOFF) {
                int sum = 0;
                for (int i = lo; i < hi; i++) {
                    sum += a[row][i] * b[i][col];
                }
                return sum;
            } else {
                // split based on range
                int mid = (lo + hi) / 2;
                DotProductTask left = new DotProductTask(a, b, row, col, lo, mid);
                DotProductTask right = new DotProductTask(a, b, row, col, mid, hi);
                left.fork();
                int rightResult = right.compute();
                int leftResult = left.join();
                return leftResult + rightResult;
            }
        }
    }

}
