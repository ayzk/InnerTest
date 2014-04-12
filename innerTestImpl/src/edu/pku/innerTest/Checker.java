package edu.pku.innerTest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import sun.tools.tree.ThisExpression;

import com.sun.tools.javac.util.List;
import com.sun.xml.internal.rngom.binary.ElementPattern;

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
	
	protected StackTraceElement addCheckStackTrace;
	protected int CheckerLine;
	
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
		this.CheckerLine=-1;
		validateExpression();
		init();
	}
	
	public Checker (int times, Object obj, Object value, String expr,int line, StackTraceElement element) {
		this.times = times;
		this.obj = obj;
		this.value = value;
		this.expr = expr;
		this.CheckerLine=line;
		this.addCheckStackTrace=element;
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
				try{
					assertEquals(this.value, expression.evaluate().getValue());
				}
				catch(AssertionError t){
					
					StackTraceElement[] elements= t.getStackTrace();
	
					ArrayList<StackTraceElement> userStackTrace= new ArrayList<StackTraceElement>();		
					int i;
					for (i=0; i<elements.length && !elements[i].getClassName().startsWith("edu.pku.innerTest"); i++)
					{
						//System.out.println(elements[i]);	
						userStackTrace.add(elements[i]);
					}
					//System.out.println(this.addCheckStackTrace);
					for (;i<elements.length && elements[i].getClassName().startsWith("edu.pku.innerTest");i++);
					
					if (elements[i-1].getClassName().equals( this.addCheckStackTrace.getClassName() )
						&& elements[i-1].getMethodName().equals( this.addCheckStackTrace.getMethodName())
						 &&   elements[i-1].getFileName().equals( this.addCheckStackTrace.getFileName()) ){
						
						userStackTrace.add(new StackTraceElement(elements[i-2].getClassName(),
								elements[i-2].getMethodName(), elements[i-2].getFileName(), this.CheckerLine));
						userStackTrace.add(this.addCheckStackTrace);
						userStackTrace.add(elements[i-1]);
					}
					for (;i<elements.length;userStackTrace.add(elements[i++]));		
					
					StackTraceElement[] elements2=userStackTrace.toArray(new StackTraceElement[userStackTrace.size()]);
					//for (StackTraceElement s: elements)
					//	System.out.println(s);

			        t.setStackTrace(elements2);
			        throw(t);
					}
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
	public void setCheckerLine(int line){
		CheckerLine=line;
	}
	
}
