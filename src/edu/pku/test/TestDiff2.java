package edu.pku.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

public class TestDiff2 {

	static Calculator cal = new Calculator();
	@Test public void cal1() {
		Controller.addCheck(8, 1, cal, 0, "max");
		assertEquals(0, cal.diff(0, 0, 0));
	}
	@Test public void cal2() {
		Controller.addCheck(8, 1, cal, 1, "max");
		assertEquals(1, cal.diff(0, 1, 1));
	}
	@Test public void cal3() {
		Controller.addCheck(8, 1, cal, 0, "max");
		assertEquals(1, cal.diff(0, 0, 1));
	}
	@Test public void cal4() {
		Controller.addCheck(8, 1, cal, 5, "max");
		assertEquals(2, cal.diff(5, 4, 6));
	}
	@Test public void cal5() {
		Controller.addCheck(8, 1, cal, 1, "max");
		assertEquals(15, cal.diff(2, 5, 17));
	}
	
	@After
	public void clean() {
		System.out.println("[INFO] " + TestMathCalculator.class
				+ " - Excuting clean...");
		Controller.clean();
	}
	
}
