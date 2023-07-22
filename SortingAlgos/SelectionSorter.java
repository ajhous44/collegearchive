package lab4;
// Modified from the code from Sedgewick & Wayne's Algorithms textbook, 4th edition. 

import static lab4.SortingHelper.*; // less, exch, isSorted methods

import java.util.Arrays;

public class SelectionSorter<T extends Comparable<T>> implements SortAlgorithm<T> {

    @Override
	public T[] sort(T[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i+1; j < n; j++) {
                if (less(a[j], a[min])) 
                	min = j;
            }
            exch(a, i, min);
            // Note: I've commented out the assert statements, since checking them slows the code down, and
            // we'll be timing it later, but...

            // VOCAB: algorithm "invariant" - what is always true after each iteration? 
            // Invariant: the array is sorted from beginning up to index i. 
            // assert isSorted(a, 0, i);   
        }
        //post-condition: everything is sorted!
        //assert isSorted(a); 
        return a;
    }

    public static void main(String[] args) {
        String[] a = new String[] {"emu", "dog", "ant", "cow", "fox", "bug" };
        String[] sorted = new SelectionSorter<String>().sort(a);
        
        System.out.println(Arrays.toString(sorted));

        assert isSorted(sorted); 
        // the assert statement is another approach to testing 
        // it's built into Java, so you can use it without importing the JUnit library.
    }
}
