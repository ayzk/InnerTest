package edu.pku.test.expression.syntax.operator;

import java.math.BigDecimal;

import edu.pku.test.expression.Expression;
import edu.pku.test.expression.syntax.ArgumentsMismatchException;
import edu.pku.test.expression.tokens.DataType;
import edu.pku.test.expression.tokens.Valuable;



public class DivideOperator extends BinaryOperator {

	public DivideOperator() {
		super("DIVIDE");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			if (a2.getNumberValue().compareTo(new BigDecimal("0")) == 0)
				throw new ArithmeticException("Divided by zero.");
			result = a1.getNumberValue().divide(a2.getNumberValue(), 
					Expression.DEFAULT_DIVISION_SCALE, Expression.DEFAULT_DIVISION_ROUNDING_MODE);
		} else {
			throw new ArgumentsMismatchException(arguments, "/");
		}
		return result;
	}
}
