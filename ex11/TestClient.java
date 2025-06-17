import java.util.Random;

public class TestClient {

    public static void main(String[] args) {
        testParallelSuffix();
        testFilterEmpty();
    }

    public static void testParallelSuffix() {
        Random rand = new Random(332);
        int[] a = new int[100];
        for (int i = 0; i < a.length; i++) {
            a[i] = rand.nextInt(1000);
        }
        int[] correct = Sequential.suffixSum(a);
        int[] given = ParallelSuffix.suffixSum(a, 1); // make sure it works for any choice of cutoff!
        if (correct.length != given.length) {
            System.out.println("Incorrect parallel suffix results");
            return;
        }
        for (int i = 0; i < correct.length; i++) {
            if (correct[i] != given[i]) {
                System.out.println("Incorrect parallel suffix results");
                return;
            }
        }
        System.out.println("Correct Parallel Suffix Results! But we will still check your parallel suffix implementation!");
    }

    public static void testFilterEmpty() {
        Random rand = new Random(434);
        String[] a = new String[100];
        for (int i = 0; i < a.length; i++) {
            if (rand.nextBoolean()) {
                a[i] = "";
            } else {
                a[i] = "" + rand.nextInt();
            }
        }
        String[] correct = Sequential.filterEmpty(a);
        String[] given = FilterEmpty.filterEmpty(a, 1); // make sure it works for any choice of cutoff!
        if (correct.length != given.length) {
            System.out.println("Incorrect filter empty results");
            return;
        }
        for (int i = 0; i < correct.length; i++) {
            if (!correct[i].equals(given[i])) {
                System.out.println("Incorrect filter empty results");
                return;
            }
        }
        System.out.println("Correct Filter Empty Results! But we will still check your filter empty implementation!");
    }
}
