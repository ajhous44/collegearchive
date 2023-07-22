package lab4;

//TODO: Michael Wardach, AJ Housholder

import static lab4.SortingHelper.*; // less, exch, isSorted methods

import java.util.Arrays;

public class BubbleSorter<T extends Comparable<T>> implements SortAlgorithm<T> {

    public BubbleSorter() { }

    @Override
	public T[] sort(T[] a) {

    	//TODO: Implement "Bubble Sort" code here.
    	int n = a.length;
    	boolean anySwaps = true;
    	while (anySwaps) {
    		anySwaps = false;
    		for (int i = 1; i < n; i++) {
    			if (less(a[i], a[i-1])) {
    				exch(a, i, i-1);
    				anySwaps = true;
    			}
    		}
    	}
    	return a;
    }

    public static void main(String[] args) {
        String[] a = new String[] {"emu", "dog", "ant", "cow", "fox", "bug" };
        new BubbleSorter<String>().sort(a);
        
        System.out.println(Arrays.toString(a));
        assert isSorted(a); // the assert statement is another tool for testing your code 
        // (it's built into Java, so you can use it without importing the JUnit library.)
    }
}
