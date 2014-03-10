package edu.pku.test.instrument.variables;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;

/**
 * 
 * @author zhutao
 * 
 */

public abstract class Variable {

	public abstract InsnList genLoadLocalInsnList(int index);
	
	public abstract InsnList genLoadFieldInsnList(String className, FieldNode fn);
	
	public abstract int getIndexStep();

}
