package edu.pku.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.After;

public class TestMathCalculator {

	static MathCalculator yc = new MathCalculator();

	@Test
	public void cal1() {
		Controller.addCheck(28, 1, yc, true, "res;");
		yc.isPositive(-1);
	}
	
	@Test
	public void cal2() {
		Controller.addCheck("locate_check_002", 1, yc, 'A', "a");

		yc.toUpper('a');
	}
	
	@Test
	public void cal3() {
		Controller.addCheck(new UserChecker(1, yc), "locate_check_003");

		yc.sum(2, 3);
	}
	@Test
	public void cal4() {
		Controller.addCheck("locate_check_004", 1, yc, "abcdf", "a+b;");

		String str1 = "abc";
		String str2 = "def";
		yc.sumString(str1, str2);
	}
	
	@Test
	public void cal5() {
		Controller.addCheck("locate_check_005", 1, yc, true, "a+b>1.0;");

		yc.mulFloat(1.0f, 2.8f);
	}
	
	@Test
	public void cal6() {
		Controller.addCheck("locate_check_006", 1, yc, true, "a+b<0;");

		yc.mulDouble(-2.0f, 3.0f);
	}
	
	@Test
	public void cal7() {
		Controller.addCheck("locate_check_007", 1, MathCalculator.class, true, "a<0;");
		Controller.addCheck("locate_check_008", 1, MathCalculator.class, 2, "a-b;");
		
		MathCalculator.sub(4, 1);
	}


	@After
	public void clean() {
		System.out.println("[INFO] " + TestMathCalculator.class
				+ " - Excuting clean...");
		Controller.clean();
	}
}
