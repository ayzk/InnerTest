package edu.pku.test.expression.syntax;

import edu.pku.test.expression.tokens.Valuable;

public interface Executable {
	
	public int getArgumentNum();
	
	public Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException;
	
}
