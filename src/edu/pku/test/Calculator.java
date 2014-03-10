package edu.pku.test;

public class Calculator {
	public int diff(int a, int b, int c) {
		int max,min;
		
		max = a < b ? a : b;
		max = max > c ? max : c;
		min = a < b ? a : b;
		min = min < c ? min : c;
		
		return max - min;
	}
}
