package edu.pku.test.instrument.variables;

import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * 
 * @author zhutao
 * 
 */

public class RefVariable extends Variable {

	@Override
	public InsnList genLoadLocalInsnList(int index) {
		// TODO Auto-generated method stub
		InsnList il = new InsnList();
		il.add(new VarInsnNode(ALOAD, index));
		
		return il;
	}

	@Override
	public int getIndexStep() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public InsnList genLoadFieldInsnList(String className, FieldNode fn) {
		// TODO Auto-generated method stub
		InsnList il = new InsnList();
		if ((fn.access & ACC_STATIC) == 0) {
			il.add(new VarInsnNode(ALOAD, 0));
			il.add(new FieldInsnNode(GETFIELD, className, fn.name,
					fn.desc));
		} else {
			il.add(new FieldInsnNode(GETSTATIC, className, fn.name,
					fn.desc));
		}
		
		return il;
	}

	

}
