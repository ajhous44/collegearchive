package lab4;

//Modified from the code from Sedgewick & Wayne's Algorithms textbook, 4th edition. 
//TODO: (YOUR NAME HERE) 


import static lab4.SortingHelper.*; // less, exch, isSorted methods

import java.util.Arrays;

public class InsertionOptimizedSorter<T extends Comparable<T>> implements SortAlgorithm<T> {

    public InsertionOptimizedSorter() { }

    @Override
	public T[] sort(T[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
        	T temp = a[i];
        	int place = i;
        	for (int j = i; j > 0 && less(temp, a[j-1]); j--) {
        		a[j] = a[j-1];
        		place = j-1;
        	
            }
        	a[place] = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        String[] a = new String[] {"emu", "dog", "ant", "cow", "fox", "bug" };
        String[] sorted = new InsertionOptimizedSorter<String>().sort(a);
        
        System.out.println(Arrays.toString(sorted));

        assert isSorted(sorted); // the assert statement is another approach to testing 
        // it's built into Java, so you can use it without importing the JUnit library.
    }
}
