package lab4;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;

import static lab4.SortingHelper.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SortingTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}
	
	void runSmallIntegerTests(SortAlgorithm<Integer> sorter) {
		// does it mess up an already sorted list? 
		Integer[] nums = new Integer[] {1,2,3,4,5};
		Integer[] sortedNums = sorter.sort(nums);
		Integer[] correct = Arrays.copyOf(nums, nums.length);
		Arrays.sort(correct);
		assertTrue(isSorted(sortedNums));
		assertArrayEquals(correct, sortedNums);

		nums = new Integer[] {5,4,3,2,1};
		correct = Arrays.copyOf(nums, nums.length);
		Arrays.sort(correct);
		sortedNums = sorter.sort(nums);
		assertTrue(isSorted(sortedNums));
		assertArrayEquals(correct, sortedNums);

		nums = new Integer[] {2,1,5,3,4};
		correct = Arrays.copyOf(nums, nums.length);
		Arrays.sort(correct);
		sortedNums = sorter.sort(nums);
		assertTrue(isSorted(sortedNums));
		assertArrayEquals(correct, sortedNums);
	}
	
	void runMoreIntegerTests(SortAlgorithm<Integer> sorter) {
				
		Integer[] nums = new Integer[] {100,-5, 14, 23, 2, 17, 12, 1000, 14, 2};
		Integer[] sortedNums = sorter.sort(nums);
		Integer[] correct = Arrays.copyOf(nums, nums.length);
		Arrays.sort(correct);
		
		assertTrue(isSorted(sortedNums));
		assertArrayEquals(correct, sortedNums);
		
		nums = new Integer[] {100,-5, 14, 23, 2, 17, 12, 1000, 14, 2, 3, 4, 9, 8, 7, 6, 22};
		correct = Arrays.copyOf(nums, nums.length);
		Arrays.sort(correct);
		sortedNums = sorter.sort(nums);
		assertTrue(isSorted(sortedNums));
		assertArrayEquals(correct, sortedNums);
		// These tests are not comprehensive -- feel free to add your 
		// own tests cases here!
	}
	
	void runIntegerTests(SortAlgorithm<Integer> sorter) {
		runSmallIntegerTests(sorter);
		runMoreIntegerTests(sorter);
	}
	
	
	@Test
	void testSelectionSort() {
		runIntegerTests(new SelectionSorter<Integer>());
		
	}

	@Test
	void testInsertionSort() {
		runIntegerTests(new InsertionSorter<Integer>());
		
	}

	@Test
	void testInsertionOptimizedSort() {
		runIntegerTests(new InsertionOptimizedSorter<Integer>());
		
	}

	@Test
	void testBogoSortSmall() {
		runSmallIntegerTests(new BogoSorter<Integer>());
	}

	@Test
	void testBogoSortLarge() {
		assertTimeoutPreemptively(Duration.ofSeconds(1), () -> runMoreIntegerTests(new BogoSorter<Integer>()));
	}
	
	@Test
	void testBubbleSort() {
		runIntegerTests(new BubbleSorter<Integer>());
		
	}

	@Test
	void testEmuSort() {
		runIntegerTests(new EmuSorter<Integer>());		
	}

}
