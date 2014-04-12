package edu.pku.test.instrument.variables;

import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * 
 * @author zhutao
 * 
 */

public class CharacterVariable extends BasicVariable {

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
		il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;"));
		
		return il;
	}

	@Override
	public InsnList genLoadLocalInsnList(int index) {
		// TODO Auto-generated method stub
		InsnList il = new InsnList();
		
		il.add(new VarInsnNode(ILOAD, index));
		il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;"));
		
		return il;
	}

	@Override
	public int getIndexStep() {
		// TODO Auto-generated method stub
		
		return 1;
	}

	
}
