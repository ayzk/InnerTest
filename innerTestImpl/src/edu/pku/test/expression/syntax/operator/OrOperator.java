package edu.pku.test.expression.syntax.operator;

import edu.pku.test.expression.syntax.ArgumentsMismatchException;
import edu.pku.test.expression.tokens.DataType;
import edu.pku.test.expression.tokens.Valuable;


public class OrOperator extends BinaryOperator {

	public OrOperator() {
		super("OR");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.BOOLEAN
				&& a2.getDataType() == DataType.BOOLEAN) {
			result = a1.getBooleanValue() || a2.getBooleanValue();
		} else {
			throw new ArgumentsMismatchException(arguments, "||");
		}
		return result;
	}

}
