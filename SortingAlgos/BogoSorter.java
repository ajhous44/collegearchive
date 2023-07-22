package lab4;

//TODO: Michael Wardach, AJ Housholder

import static lab4.SortingHelper.*; // less, exch, isSorted methods

import java.util.Arrays;

public class BogoSorter<T extends Comparable<T>> implements SortAlgorithm<T> {

    public BogoSorter() { }

    @Override
	public T[] sort(T[] a) {

    	//TODO: Implement randomized "Bogo Sort" code here.
    	// use these static methods from the SortingHelper class:
    	//  - isSorted(array)
    	//  - shuffle(array)
    	while (!isSorted(a)) {
    		shuffle(a);
    	}
    	
    	return a;
    }

    public static void main(String[] args) {
        String[] a = new String[] {"emu", "dog", "ant", "cow", "fox", "bug" };
        new BogoSorter<String>().sort(a);
        
        System.out.println(Arrays.toString(a));
        assert isSorted(a); // the assert statement is another tool for testing your code 
        // (it's built into Java, so you can use it without importing the JUnit library.)
    }
}
