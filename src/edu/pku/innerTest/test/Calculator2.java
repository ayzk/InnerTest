package edu.pku.innerTest.test;

/**
 * 
 * @author zhutao
 * 
 */

public class Calculator2 {
	
	// Calculate the gcd of biggest and smallest
	public int test (int a, int b, int c) {
		int max, min, tmp;
		
		max = getMax(a, b, c);
		min = getMin(a, b, c);
		
		// greatest common divider
		while (min != 0) {
			tmp = min;
			min = max % min;
			max = tmp;
		}
		
		return max;
	}
	
	public int tt = 1;
	public int getMax(int a, int b, int c) {
		int max = a < b ? a : b;
		max = max < c ? max : c;
		return max;
	}
	public int getMin(int a, int b, int c) {
		int min = a < b ? a : b;
		min = min < c ? min : c;
		return min;
	}
	
	public int diff(int a, int b, int c) {
		int max,min;
		
		max = a < b ? a : b;
		max = max > c ? max : c;
		
		min = a < b ? a : b;
		min = min < c ? min : c;
		
		return max - min;
	}
}
