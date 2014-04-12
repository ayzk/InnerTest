package edu.pku.test.instrument.variables;

/**
 * 
 * @author zhutao
 * 
 */

public class VariableFactory {
	private static VariableFactory variableFactory = new VariableFactory();
	
	private VariableFactory(){}
	
	public static VariableFactory getInstance() {
		return variableFactory;
	}
	
	public Variable getVariable(String desc) {
		if (desc.compareTo("I") == 0) {
			return new IntegerVariable();
		} else if (desc.compareTo("F") == 0) {
			return new FloatVariable();
		} else if (desc.compareTo("Z") == 0) {
			return new BooleanVariable();
		} else if (desc.compareTo("C") == 0) {
			return new CharacterVariable();
		} else if (desc.compareTo("B") == 0) {
			return new ByteVariable();
		} else if (desc.compareTo("J") == 0) {
			return new LongVariable();
		} else if (desc.compareTo("D") == 0) {
			return new DoubleVariable();
		} else {
			return new RefVariable();
		}
	}
}
