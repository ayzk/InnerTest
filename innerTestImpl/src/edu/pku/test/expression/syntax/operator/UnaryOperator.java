package edu.pku.test.expression.syntax.operator;

public abstract class UnaryOperator extends Operator {

	public UnaryOperator(String operatorName) {
		super(operatorName);
	}

	public final int getArgumentNum() {
		return 1;
	}

}
