import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FilterEmpty {
    static ForkJoinPool POOL = new ForkJoinPool();
    static int CUTOFF;

    public static String[] filterEmpty(String[] arr, int cutoff) {
        FilterEmpty.CUTOFF = cutoff;
        int[] mapped = new int[arr.length];
        POOL.invoke(new MapEmptyTask(arr, mapped, 0, arr.length));
        int[] suffixSum = ParallelSuffix.suffixSum(mapped, cutoff);
        String[] filtered = new String[suffixSum[0]];
        POOL.invoke(new CopyFilteredTask(arr, suffixSum, mapped, filtered, 0, arr.length));
        return filtered;
    }

    private static class MapEmptyTask extends RecursiveAction {
        private String[] arr;
        private int[] output;
        private int lo;
        private int hi;

        public MapEmptyTask(String[] arr, int[] output, int lo, int hi) {
            this.arr = arr;
            this.output = output;
            this.lo = lo;
            this.hi = hi;
        }

        public void compute() {
            if ((hi - lo) <= FilterEmpty.CUTOFF) {
                // base case
                // map the elements based on wheater empty or not
                for (int i = lo; i < hi; i++) {
                    if (!arr[i].isEmpty()) {
                        output[i] = 1;
                    } else {
                        output[i] = 0;
                    }
                }

            } else {
                int mid = (hi + lo) / 2;
                MapEmptyTask left = new MapEmptyTask(arr, output, lo, mid);
                MapEmptyTask right = new MapEmptyTask(arr, output, mid, hi);
                left.fork();
                right.compute();
                left.join();

            }

        }
    }

    private static class CopyFilteredTask extends RecursiveAction {
        String[] arr;
        int[] suffixSum;
        int[] mapped;
        String[] output;
        int hi;
        int lo;

        public CopyFilteredTask(String[] arr, int[] suffixSum, int[] mapped, String[] output, int lo, int hi) {
            this.arr = arr;
            this.suffixSum = suffixSum;
            this.mapped = mapped;
            this.output = output;
            this.hi = hi;
            this.lo = lo;
        }

        public void compute() {
            // Base case
            // Use the suffix sum to determine the index in the output array
            // for each element in the input array. The suffix sum will tell you
            // how many non-empty elements have been copied so far.
            // You can use the mapped array to determine if an element is empty or not.
            if ((hi - lo) <= FilterEmpty.CUTOFF) {
                int count = 0;
                for (int i = lo; i < hi; i++) {
                    if (mapped[i] == 1) {
                        // how many non-empty strings come before index i
                        int j = suffixSum[0] - suffixSum[i];
                        output[j] = arr[i];
                    }
                }
            } else {
                int mid = (hi + lo) / 2;
                CopyFilteredTask left = new CopyFilteredTask(arr, suffixSum, mapped, output, lo, mid);
                CopyFilteredTask right = new CopyFilteredTask(arr, suffixSum, mapped, output, mid, hi);
                left.fork();
                right.compute();
                left.join();

            }
        }
    }
}
