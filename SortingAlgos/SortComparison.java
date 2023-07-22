package lab4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortComparison {

	public static Double[] getRandomArray(int arrayLength) {
		Double[] result = new Double[arrayLength];
		for (int i = 0; i < result.length; i++) {
			result[i] = Math.random();
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		List<SortAlgorithm<Double>> sorters = Arrays.asList(
				new SelectionSorter<Double>(), 
				new InsertionSorter<Double>(),
				new InsertionOptimizedSorter<Double>(),
				new BubbleSorter<Double>(),
				new EmuSorter<Double>()
				);

		// TODO: change the numbers as necessary to get 
		//       timing data that makes a decent plot/chart.

		// let's go up linearly, instead of doubling, to see if we can 
		// get better parabolas for the quadratic sorts...
		for (int n = 5000; n <= 50000; n += 5000) {
			Double[] originalArray = getRandomArray(n);  
			
			for (SortAlgorithm<Double> sorter : sorters) {
				Double[] copy = Arrays.copyOf(originalArray, originalArray.length);
				
				long startTime = System.currentTimeMillis();			
				sorter.sort(copy);
				long endTime = System.currentTimeMillis();
				double sortingTime = (endTime - startTime) / 1000.0;
				System.out.printf("%33s, %10d, %.9f\n", sorter.getClass().getName(), n,  sortingTime);
			}
		}
		
		

	}

}
