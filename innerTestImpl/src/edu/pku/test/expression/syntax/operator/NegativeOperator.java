package edu.pku.test.expression.syntax.operator;

import java.math.BigDecimal;

import edu.pku.test.expression.syntax.ArgumentsMismatchException;
import edu.pku.test.expression.tokens.DataType;
import edu.pku.test.expression.tokens.Valuable;



public class NegativeOperator extends UnaryOperator {

	public NegativeOperator() {
		super("NEGATIVE");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable argument = arguments[0];
		if (argument.getDataType() == DataType.NUMBER) {
			result = new BigDecimal("0").subtract(argument.getNumberValue());
		} else {
			throw new ArgumentsMismatchException(arguments, "-");
		}
		return result;
	}

}
