package edu.pku.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.After;

/**
 * 
 * @author zhutao
 * 
 */

public class TestCalculator {
	
	static Calculator2 cal = new Calculator2();
	
	
	@Test
	public void cal1() {
		Controller.addCheck(8, 1, cal, 2, "max");
//		Controller.addCheck(9, 1, cal, 2, "min");
		assertEquals(2, cal.test(2, 2, 2));
	}
	
	@Test
	public void cal2() {
		Controller.addCheck(8, 1, cal, 1, "max");
//		Controller.addCheck(9, 1, cal, 1, "min");
		assertEquals(1, cal.test(1, 1, 1));
		
	}
	
	@Test
	public void cal3() {
		Controller.addCheck(8, 1, cal, 1, "tt");
//		Controller.addCheck(9, 1, cal, 2, "min");
		assertEquals(2, cal.test(4, 2, 6));
	}
	
	@Test
	public void cal4() {
		Controller.addCheck(8, 1, cal, 6, "max");
//		Controller.addCheck(9, 1, cal, 4, "min");
		assertEquals(2, cal.test(4, 5, 6));
	}
	
	@After
	public void clean() {
		System.out.println("[INFO] " + TestMathCalculator.class
				+ " - Excuting clean...");
		Controller.clean();
	}
	
}
