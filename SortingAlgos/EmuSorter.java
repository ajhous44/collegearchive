package lab4;

// TODO: (YOUR NAME HERE) 

import static lab4.SortingHelper.*; // less, exch, isSorted methods

import java.util.Arrays;

public class EmuSorter<T extends Comparable<T>> implements SortAlgorithm<T> {

    public EmuSorter() { }

    @Override
	public T[] sort(T[] a) {

    	//TODO: Implement "Emu Sort" code here.
    	int n = a.length;
    	int[] Count = new int[n];
    	T[] result = a.clone();
    	for (int i = 0; i < n; i++) {
    		Count[i] = 0;
    	}
    	for (int i = 0; i < n-1; i++) {
    		for (int j = i+1; j < n; j++) {
    			if (less(a[i], a[j])) {
    				Count[j] = Count[j] + 1;
    			} else {
    				Count[i] = Count[i] + 1;
    			}
    		}
    	}
    	for (int i = 0; i < n; i++) {
    		result[i] = a[i];
    	}
    	return result;
    }

    public static void main(String[] args) {
        String[] a = new String[] {"emu", "dog", "ant", "cow", "fox", "bug" };
        new EmuSorter<String>().sort(a);
        
        System.out.println(Arrays.toString(a));
        assert isSorted(a); // the assert statement is another tool for testing your code 
        // (it's built into Java, so you can use it without importing the JUnit library.)
    }
}
