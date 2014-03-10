package edu.pku.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import edu.pku.test.checker.Checker;
import edu.pku.test.constants.ActionKeys;
import edu.pku.test.instrument.Instrumentor;
import edu.pku.test.instrument.InstrumentorConfig;
import edu.pku.test.util.ClassSwapper;

/**
 * 
 * @author zhu.tao
 *
 */

public class Controller {
	private static Controller controller = null;
	private List<Checker> checkers;
	private Map<String, byte[]> originalClasses;
	
	private Controller () {
		checkers = new ArrayList<Checker>();
		originalClasses = new HashMap<String, byte[]>();
	}
	
	private static void createInstance() {
		if (controller == null) {
			controller = new Controller();
		}
	}

	/**
	 * Clean the back-up byte code of classes in "originalClasses"
	 * when a test method is finished.
	 * 
	 * Be sure to add this method in method After or tearDown(Junit3). 
	 */
	public static void clean () {
		createInstance();
		ClassPool cp = ClassPool.getDefault();
		for (Entry<String, byte[]> entry:controller.originalClasses.entrySet()) {
			try {
				ClassSwapper.swap(entry.getKey(), cp.get(entry.getKey()).toBytecode());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CannotCompileException e) {
				e.printStackTrace();
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		controller.originalClasses.clear();
		controller.checkers.clear();
	}
	
	/**
	 * The interface for end user.
	 * To add a check expression like assertion in Junit, but more detailed.
	 */
	public static int addCheck(int lineNo, int times, Object obj, Object value, String expr) {
		createInstance();
		Checker checker = new Checker(times, obj, value, expr);
		int objectType = checker.getObjectType();
		int checkerId = controller.checkers.size();
		InstrumentorConfig ic = new InstrumentorConfig(checker.getVariableNames(), lineNo, checkerId, ActionKeys.CHECK_TYPE_GENERAL, objectType);

		return add(checker, ic);
	}
	
	public static int addCheck (Checker uc, int lineNo) {
		createInstance();
		int objectType = uc.getObjectType();
		int checkerId = controller.checkers.size();
		InstrumentorConfig ic = new InstrumentorConfig(null, lineNo, checkerId, ActionKeys.CHECK_TYPE_GENERAL, objectType);
		
		return add(uc, ic);
	}
	
	public static int addCheck(String locationKey, int times, Object obj, Object value, String expr) {
		createInstance();
		Checker checker = new Checker(times, obj, value, expr);
		int objectType = checker.getObjectType();
		int checkerId = controller.checkers.size();
		InstrumentorConfig ic = new InstrumentorConfig(checker.getVariableNames(), locationKey, checkerId, ActionKeys.CHECK_TYPE_EXPR, objectType);

		return add(checker, ic);
	}
	
	public static int addCheck (Checker uc, String locationKey) {
		createInstance();
		int objectType = uc.getObjectType();
		int checkerId = controller.checkers.size();
		InstrumentorConfig ic = new InstrumentorConfig(null, locationKey, checkerId, ActionKeys.CHECK_TYPE_GENERAL, objectType);
		
		return add(uc, ic);
	}
	
	/*
	 * 
	 */
	public static int addCheck(String methodName, int relativeLineNo, int times, Object obj, Object value, String expr) {
		createInstance();
		Checker checker = new Checker(times, obj, value, expr);
		int objectType = checker.getObjectType();
		int checkerId = controller.checkers.size();
		InstrumentorConfig ic = new InstrumentorConfig(checker.getVariableNames(), methodName, relativeLineNo, checkerId, ActionKeys.CHECK_TYPE_EXPR, objectType);

		return add(checker, ic);
	}
	
	private static int add (Checker checker, InstrumentorConfig ic) {
		createInstance();
		controller.checkers.add(checker);
		String className = checker.getClassName();
		
		// back up the original byte code
		byte[] tb = ClassSwapper.getByteCode(className);
		if (!controller.originalClasses.containsKey(className)) {
			controller.originalClasses.put(className, tb);
		}
		
		//System.out.println("[class name]"+className);
	
		// Instrumentation
		tb = Instrumentor.instrument(tb, ic);

		// Hot swap, update the class in JVM by the instrumented class
		ClassSwapper.swap(className, tb);


		return controller.checkers.size() - 1;
	}
	
	/**
	 * The call back method to check the predefined assertion when program executing in some place
	 *  
	 * @param obj
	 * @param values
	 * @param checkerId
	 */
	public static void handleCheckExpr (Object obj, Map<String, Object> values, int checkerId) {
		createInstance();
		System.out.print("edu.pku.test.Controller.handleCheckExpr-[DEBUG]");
		System.out.println("<checkerid:"+checkerId+"><obj_now:"+obj+"><obj_tocheck:"+controller.checkers.get(checkerId).getObject()+">");
		controller.checkers.get(checkerId).checkExpression(obj, values);
	}
	
	/*
	 * to locate
	 */
	public static void markLocation(String idString) {
	}
}
