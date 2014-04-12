package edu.pku.test.expression.syntax.operator;

import edu.pku.test.expression.syntax.ArgumentsMismatchException;
import edu.pku.test.expression.syntax.Executable;
import edu.pku.test.expression.tokens.TokenBuilder;
import edu.pku.test.expression.tokens.Valuable;


public abstract class Operator implements Executable {

	private final String operatorName;

	public Operator(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public Valuable execute(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = operate(arguments);
		return TokenBuilder.buildRuntimeValue(result);
	}

	protected abstract Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException;

}
