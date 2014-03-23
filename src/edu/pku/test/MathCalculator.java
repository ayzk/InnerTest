package edu.pku.test;

public class MathCalculator {
	
	 String static_string = "pku";
	 static public int static_int=1;
//	 public int int1;
	
//	public void test() {
//		int a = 1;
//		long b = 2;
//		a = a + 1;
//		String str1 = "abc";
//		String str2 = "def";
//		java.util.HashMap<String, Object> checkTmpMap = new java.util.HashMap<String, Object>();
//		checkTmpMap.put("str1", str1);
//		checkTmpMap.put("str2", str2);
//		checkTmpMap.put("b", b);
//		checkTmpMap.put("a", a);
//		Controller.handleCheckExpr(null, checkTmpMap, 0);
//	}
	
	public boolean isPositive(int a) {
		boolean res;
		if (a > 0) {
			res = true;
		} else {
			res = false;
		}
		Controller.markLocation("locate_check_001");
		
		return res;
	}
	
	public char toUpper(char a) {
		if (a >= 'a' && a <= 'z') {
			a = (char)(a + 'A' - 'a');
		} 
		Controller.markLocation("locate_check_002");
		return a;
	}
	
	public int sum (int a, int b) {
		int res=a+b;
		Controller.markLocation("locate_check_003");
		return  res;
	}
	
	public String sumString (String a, String b) {
		String res = a + b;
		Controller.markLocation("locate_check_004");
		return res;
	}
	
	public float mulFloat (float a, float b) {
		float res = a + b;
		Controller.markLocation("locate_check_005");
		return res;
	}
	
	public double mulDouble (double a, double b) {
		double res = a + b;
		Controller.markLocation("locate_check_006");
		return res;
	}
	

	public  int sub(int a, int b) {
		int res = a - b;
		Controller.markLocation("locate_check_007");
		Controller.markLocation("locate_check_008");
		return res;
	}
	
	public int cal9(int a){
		int int1=a;
		Controller.markLocation("locate_check_009");
		Controller.markLocation("locate_check_010");
		Controller.markLocation("locate_check_011");
		Controller.markLocation("locate_check_012");
		return int1;
		
	}
	
		
}
