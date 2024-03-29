
public class FlagSort 
{

    private static final int NUMBER_OF_BUCKETS = 10; // 10 for base 10 numbers

    private FlagSort() { }

    public static Integer[] sort(Integer[] unsorted) {
        int numberOfDigits = getMaxNumberOfDigits(unsorted); // Max number of digits
        int max = 1;
        for (int i = 0; i < numberOfDigits - 1; i++)
            max *= 10;
        sort(unsorted, 0, unsorted.length, max);
        return unsorted;
    }

    public static void sort(Integer[] unsorted, int start, int length, int divisor) {
        // First pass - find counts
        int[] count = new int[NUMBER_OF_BUCKETS];
        int[] offset = new int[NUMBER_OF_BUCKETS];
        int digit = 0;
        for (int i = start; i < length; i++) {
            int d = unsorted[i];
            digit = getDigit(d, divisor);
            count[digit]++;
        }
        offset[0] = start + 0;
        for (int i = 1; i < NUMBER_OF_BUCKETS; i++) {
            offset[i] = count[i - 1] + offset[i - 1];
        }
        // Second pass - move into position
        for (int b = 0; b < NUMBER_OF_BUCKETS; b++) {
            while (count[b] > 0) {
                int origin = offset[b];
                int from = origin;
                int num = unsorted[from];
                unsorted[from] = -1;
                do {
                    digit = getDigit(num, divisor);
                    int to = offset[digit]++;
                    count[digit]--;
                    int temp = unsorted[to];
                    unsorted[to] = num;
                    num = temp;
                    from = to;
                } while (from != origin);
            }
        }
        if (divisor > 1) {
            // Sort the buckets
            for (int i = 0; i < NUMBER_OF_BUCKETS; i++) {
                int begin = (i > 0) ? offset[i - 1] : start;
                int end = offset[i];
                if (end - begin > 1)
                    sort(unsorted, begin, end, divisor / 10);
            }
        }
    }

    private static int getMaxNumberOfDigits(Integer[] unsorted) {
        int max = Integer.MIN_VALUE;
        int temp = 0;
        for (int i : unsorted) {
            temp = (int) Math.log10(i) + 1;
            if (temp > max)
                max = temp;
        }
        return max;
    }

    private static int getDigit(int integer, int divisor) {
        return (integer / divisor) % 10;
    }
}

/*Average case = O(n*k/d) Worst case = O(n*k/d) Best case = O(n*k/d) 
 * NOTE: n is the number of digits and k is the average bucket size */
