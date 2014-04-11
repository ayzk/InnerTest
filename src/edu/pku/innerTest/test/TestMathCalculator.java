package edu.pku.innerTest.test;

import org.junit.Test;
import org.junit.After;

import edu.pku.innerTest.Controller;
import edu.pku.innerTest.UserChecker;

public class TestMathCalculator {

	 MathCalculator yc = new MathCalculator();

	@Test
	public void cal1() {
		//Controller.addCheck(28, 1, yc, true, "res");
		Controller.addCheck("locate_check_001",1,yc,true,"res");
		//yc.isPositive(-1);
	}
	
	@Test
	public void cal2() {
		Controller.addCheck("locate_check_002", 1, yc, 'A', "a");

		yc.toUpper('a');
	}
	
	@Test
	public void cal3() {
		Controller.addCheck(new UserChecker(1, yc), "locate_check_003");
//		Controller.addCheck("locate_check_003", 1, yc, true, "res<6;" );
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
		Controller.addCheck("locate_check_005", 1, yc, true, "res>1.0;");

		yc.mulFloat(1.0f, 2.8f);
	}
	
	@Test
	public void cal6() {
		Controller.addCheck("locate_check_006", 1, yc, true, "a+b<0;");

		yc.mulDouble(-2.0f, 3.0f);
	}
	
	@Test
	public void cal7() {
		Controller.addCheck("locate_check_007", 1, yc, 1, "static_int-a;");
		//Controller.addCheck("locate_check_008", 1, MathCalculator.class, 2, "a-b;");
		
		yc.sub(1, 1);
	}
	
	@Test
	public void cal8() {
		Controller.addCheck("locate_check_007", 1, yc, 1, "a-b;");
		Controller.addCheck("locate_check_008", 1, MathCalculator.class, 2, "a-b;");
		
		yc.sub(3, 1);
	}
	
	
	@Test
	public void cal9(){
		MathCalculator yc2 = new MathCalculator();
		Controller.addCheck("locate_check_009", 1, yc2, 0, "int1;");
		
		Controller.addCheck("locate_check_010", 1, yc, 1, "int1;");
		Controller.addCheck("locate_check_011", 1, MathCalculator.class, 1, "int1;");
		
		//yc2.cal9(1);
		//yc2.cal9(1);
		//System.out.print("yc is ");
		//System.out.println((Object)yc);
		//System.out.println((Object)yc2);
		
		System.out.println(yc2.cal9(0));
		System.out.println(yc.cal9(0));
		
	}
	@Test
	public void cal10(){
		Controller.addCheck("locate_check_012", yc, "int1==1");
		yc.cal9(0);
	}

	@After
	public void clean() {
		System.out.println("[INFO] " + TestMathCalculator.class
				+ " - Excuting clean...");
		Controller.clean();
	}
}
