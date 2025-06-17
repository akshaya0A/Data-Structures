public class Sequential {
    public static int[] suffixSum(int[] arr) {
        int[] result = new int[arr.length];
        if (arr.length == 0) {
            return result;
        }
        result[arr.length - 1] = arr[arr.length - 1];
        for (int i = arr.length - 2; i >= 0; i--) {
            result[i] = result[i + 1] + arr[i];
        }
        return result;
    }

    public static String[] filterEmpty(String[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].isEmpty()) {
                ++count;
            }
        }
        String[] filtered = new String[count];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].isEmpty()) {
                filtered[index++] = arr[i];
            }
        }
        return filtered;
    }
}
