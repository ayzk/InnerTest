package edu.pku.test.checker;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.pku.test.constants.ActionKeys;
import edu.pku.test.expression.Expression;
import edu.pku.test.expression.ExpressionFactory;

/**
 * 
 * @author zhutao
 * 
 */

public class Checker {
	
	protected int times = 1;
	protected int currentTimes = 0;
	protected String className;
	protected Object obj = null;
	protected int objectType;
	private Object value;
	private String expr;
	
	
	
	public Checker(int times, Object obj) {
		this.obj = obj;
		this.times = times;
		init();
	}
		
	public Checker (int times, Object obj, Object value, String expr) {
		this.times = times;
		this.obj = obj;
		this.value = value;
		this.expr = expr;
		validateExpression();
		init();
	}
	
	@SuppressWarnings("rawtypes")
	private void init() {
		if (obj instanceof Class) {
			// for class
			 className = ((Class)obj).getName();
			 objectType = ActionKeys.CHECK_INSN_CLASS;
			 obj = null;
			 
		} else {
			// for object
			className = obj.getClass().getName();
			objectType = ActionKeys.CHECK_INSN_OBJ;
		}
	}
	
	private void validateExpression() {
		if(! this.expr.endsWith(";")) {
			this.expr += ";";
		}
	}
	/*
	 * The default check method, user can rewrite in subclass
	 */
	public void checkExpression(Object objectToCheck, Map<String, Object> values) {
		if (objectToCheck == this.obj) {
			currentTimes += 1;
			if (currentTimes == times) {
				ExpressionFactory factory = ExpressionFactory.getInstance();
				Expression expression = factory.getExpression(expr);
				for(Entry<String, Object> entry : values.entrySet()) {
					expression.setVariableValue(entry.getKey(), entry.getValue());
					
				}
				assertEquals(this.value, expression.evaluate().getValue());
			}
		}
	}
	
	/**
	 * Get variable names in expression except instant numbers
	 */
	public Set<String> getVariableNames () {
		ExpressionFactory factory = ExpressionFactory.getInstance();
		Expression expression = factory.getExpression(expr);
		Set<String> variableNames = expression.getVariableNames();
		for (String name : variableNames) {
			if (name.charAt(0) <= '9' && name.charAt(0) >= '0') {
				variableNames.remove(name);
			}
		}
		return variableNames;
	}
	
	/*
	 * Currently not in use
	 */
	public void check(Object obj, Object value) {
		if (obj == this.obj){
			currentTimes += 1;
			System.out.println("Checker [INFO]: expected: " + this.value + ", checked: " + value + ", times: " + currentTimes);
			if (currentTimes == times) {
				assertEquals(this.value, value);
			}
		}
	}
	
	public String getClassName() {
		return className;
	}
	
	public int getObjectType() {
		return objectType;
	}
	
	public Object getObject(){
		return obj;
	}
	
}
