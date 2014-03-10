package edu.pku.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

public class TestDiff {

	static Calculator2 cal = new Calculator2();
	@Test public void cal1() {
		assertEquals(0, cal.diff(0, 0, 0));
	}
	@Test public void cal2() {
		assertEquals(1, cal.diff(0, 1, 1));
	}
	@Test public void cal3() {
		assertEquals(1, cal.diff(0, 0, 1));
	}
	@Test public void cal4() {
		assertEquals(2, cal.diff(5, 4, 6));
	}
	@Test public void cal5() {
		assertEquals(15, cal.diff(2, 5, 17));
	}
}
