package edu.pku.test.expression;

import edu.pku.test.expression.Expression;
import edu.pku.test.expression.ExpressionFactory;

/**
 * 
 * @author zhutao
 * 
 */

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String a = "\\";
		System.out.println(a);
		
//		System.out.println(a.matches("[\\]"));
		
		ExpressionFactory factory = ExpressionFactory.getInstance();
		Expression expression = factory.getExpression("c=true;a=false;\na||c;");
//		expression.setVariableValue("a", 2);
//		expression.setVariableValue("b", 3);
		System.out.println(expression.evaluate().getValue());
		
	}

}
