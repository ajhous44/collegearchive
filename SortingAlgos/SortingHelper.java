package lab4;

public class SortingHelper {
	/***************************************************************************
	 * Helper sorting functions.
	 ***************************************************************************/

	// is v < w ?
	public static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	// exchange a[i] and a[j]
	public static void exch(Object[] a, int i, int j) {
		Object swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	/***************************************************************************
	 * Check if array is sorted - useful for debugging.
	 ***************************************************************************/
	public static boolean isSorted(Comparable[] a) {
		return isSorted(a, 0, a.length);
	}

	// is the array a[lo..hi) sorted
	public static boolean isSorted(Comparable[] a, int lo, int hi) {
		for (int i = lo + 1; i < hi; i++)
			if (less(a[i], a[i - 1]))
				return false;
		return true;
	}

	/**
	 *  FROM https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Knuth.java.html
     * Rearranges an array of objects in uniformly random order
     * (under the assumption that {@code Math.random()} generates independent
     * and uniformly distributed numbers between 0 and 1).
     * @param a the array to be shuffled
     */
    public static void shuffle(Object[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            // choose index uniformly in [0, i]
            int r = (int) (Math.random() * (i + 1));
            Object swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
    }
	
	
}
