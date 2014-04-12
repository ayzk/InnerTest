package edu.pku.test.instrument.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * 
 * @author zhutao
 * 
 */

public class ByteCodeUtil {
	
	public static LabelNode getLastLabelNode (MethodNode mn) {
		LabelNode res = null;
		Iterator<AbstractInsnNode> it = mn.instructions.iterator();
		while (it.hasNext()) {
			AbstractInsnNode in = it.next();
			if (in.getType() == AbstractInsnNode.LABEL) {
				res = (LabelNode)in;
			}
		}
		return res;
	}
	
	public static FieldNode getFieldNodeByName(ClassNode cn, String fieldName) {
		for (FieldNode field:(List<FieldNode>)cn.fields) {
			if (field.name.compareTo(fieldName) == 0) {
				return field;
			}
		}
		return null;
	}
	
	public static Set<String> getFieldNames(ClassNode cn) {
		List<FieldNode> fields = cn.fields;
		Set<String> fieldNames = new HashSet<String>();
		for (FieldNode fn : fields) {
			fieldNames.add(fn.name);
		}
		return fieldNames;
	}
	
	public static LocalVariableNode getLocalVariableByName(MethodNode mn, String variableName) {
		for (LocalVariableNode localVariable:(List<LocalVariableNode>)mn.localVariables) {
			if (localVariable.name.compareTo(variableName) == 0) {
				return localVariable;
			}
		}
		return null;
	}
	
	public static Set<String> getVariableNames(MethodNode mn) {
		List<LocalVariableNode> localVariables = mn.localVariables;
		Set<String> variableNames = new HashSet<String>();
		for (LocalVariableNode lvn : localVariables) {
			variableNames.add(lvn.name);
		}
		return variableNames;
	}
	
	public static LocalVariableNode getLastLocalVariable(MethodNode mn) {
		List<LocalVariableNode> localVariables = mn.localVariables;
		if (localVariables.size() == 0) {
			return null;
		}
		return localVariables.get(localVariables.size() - 1);
		
	}
}
