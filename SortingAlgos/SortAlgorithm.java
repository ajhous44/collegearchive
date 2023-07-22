package lab4;

public interface SortAlgorithm<T extends Comparable<T>> {

	/**
	 * Rearranges the array in ascending order, using the "natural order" (i.e.
	 * using the Comparable's (T's) compareTo(...) method).
	 * 
	 * @param a the array to be sorted
	 * @return the sorted array (which may be a reference to the same input array if the algorithm is "in place") 
	 */
	public T[] sort(T[] a);

	
}
