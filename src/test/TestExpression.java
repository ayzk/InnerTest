package test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import edu.pku.test.expression.Expression;
import edu.pku.test.expression.ExpressionFactory;
import edu.pku.test.expression.tokens.Valuable;

public class TestExpression {
	public static void main (String[] args) {
		ExpressionFactory factory = ExpressionFactory.getInstance();
		Expression expression = factory.getExpression("\"ab\"+\"cd\";");
		expression.setVariableValue("a", (Object)(1));
		expression.setVariableValue("b", 2);
		Valuable temp = expression.evaluate();
		
		System.out.println(temp.getNumberValue());
		Set<String> stringlist = expression.getVariableNames();
		for (String str:stringlist) {
			System.out.println(str);
		}
		assertEquals(temp.getValue(), 5);
		
	}

	
}
