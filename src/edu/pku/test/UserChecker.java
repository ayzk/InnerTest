package edu.pku.test;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import edu.pku.test.checker.Checker;
import edu.pku.test.constants.ActionKeys;

public class UserChecker extends Checker {

	public UserChecker (int times, Object obj) {
		super(times, obj);
	}
 	
	/*
	 * The user define check method
	 */
	@Override
	public void checkExpression(Object obj, Map<String, Object> variables){
		this.currentTimes ++;
		
		if ((this.obj == null || this.obj == obj) && (this.times == ActionKeys.CHECK_ANY_TIME || this.currentTimes == this.times)) {
			System.out.println("[INFO] " + UserChecker.class + " - The subclass of Checker is executing");
			int a = (Integer)variables.get("a");
			int b = (Integer)variables.get("b");
			int res =(Integer)variables.get("res");
		
			assertEquals(a + b, res);
		}
	}
}
