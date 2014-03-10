package edu.pku.test.util;

import java.io.IOException;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.util.HotSwapper;

/**
 * 
 * @author zhutao
 * 
 */

public class ClassSwapper {
	
	static HotSwapper hs = null;
	
	private ClassSwapper() {		
	}
	
	private static void createInstance() {
		if (hs == null) {
			try {
				hs = new HotSwapper(8000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalConnectorArgumentsException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static byte[] getByteCode(String className) {
		ClassPool cp = ClassPool.getDefault();
		CtClass cc;
		byte[] tb = null;
		try {
			cc = cp.get(className);
			tb = cc.toBytecode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tb;
	}
	
	/**
	 * Replace the byte code of className in JVM with new byte code in newBytecode
	 * @param className
	 * @param bytecode
	 */
	public static void swap (String className, byte[] newBytecode) {
		createInstance();
		if (hs == null) {
			System.out.println("hs is null");
		}
		hs.reload(className, newBytecode);
	}
}
