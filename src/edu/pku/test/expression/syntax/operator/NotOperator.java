package edu.pku.test.expression.syntax.operator;

import edu.pku.test.expression.syntax.ArgumentsMismatchException;
import edu.pku.test.expression.tokens.DataType;
import edu.pku.test.expression.tokens.Valuable;


public class NotOperator extends UnaryOperator {

	public NotOperator() {
		super("NOT");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable argument = arguments[0];
		if (argument.getDataType() == DataType.BOOLEAN) {
			result = !argument.getBooleanValue();
		} else {
			throw new ArgumentsMismatchException(arguments, "!");
		}
		return result;
	}

}
