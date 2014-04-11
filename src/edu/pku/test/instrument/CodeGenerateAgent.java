package edu.pku.test.instrument;

import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

import java.util.HashMap;
import java.util.Set;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import edu.pku.innerTest.ActionKeys;
import edu.pku.test.instrument.utils.ByteCodeUtil;
import edu.pku.test.instrument.variables.VariableFactory;

/**
 * 
 * @author zhutao
 * 
 */

public class CodeGenerateAgent implements CodeGeneratable {

	private static HashMap<String, InstructionType> typeMap = new HashMap<String, InstructionType>();

	static {
		//ZCBSIFJD
		typeMap.put("I", new InstructionType("I", "Integer", ILOAD, 1));
		typeMap.put("F", new InstructionType("F", "Float", FLOAD, 1));
		typeMap.put("Z", new InstructionType("Z", "Boolean", ILOAD, 1));
		typeMap.put("C", new InstructionType("C", "Character", ILOAD, 1));
		typeMap.put("B", new InstructionType("B", "Byte", ILOAD, 1));
		typeMap.put("J", new InstructionType("J", "Long", LLOAD, 2));
		typeMap.put("D", new InstructionType("D", "Double", DLOAD, 2));
	}
	
	
	@Override
	public CodeTemplate generate(ClassNode cn, InstrumentorConfig ic) {
		// TODO Auto-generated method stub
		
		// Generate code
		
		return null;
	}
	
	public InsnList GenerateInsnList(ClassNode cn, MethodNode mn, LineNumberNode lnn, int id, int objectType, Set<String> attrs) {
		InsnList il = new InsnList();
		
		LabelNode last = ByteCodeUtil.getLastLabelNode(mn);
		LocalVariableNode localVariable = ByteCodeUtil.getLastLocalVariable(mn);
				
		//int indexStep = localVariable.index + VariableFactory.getInstance().getVariable(localVariable.desc).getIndexStep();
		int indexStep;
		if (typeMap.containsKey(localVariable.desc)) {
			indexStep = localVariable.index + typeMap.get(localVariable.desc).indexStep;
		}  else {
			indexStep = localVariable.index + 1;
		}
		
		LocalVariableNode tmpMapNode = new LocalVariableNode("checkTmpMap","Ljava/util/HashMap;","Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/Object;>;",lnn.start, last, indexStep);

		mn.localVariables.add(tmpMapNode);
		
		il.add(new TypeInsnNode(NEW,"java/util/HashMap"));
		il.add(new InsnNode(DUP));
		il.add(new MethodInsnNode(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V"));
		
		int mapVariableIndex = ByteCodeUtil.getLocalVariableByName(mn, "checkTmpMap").index;
		il.add(new VarInsnNode(ASTORE, mapVariableIndex));
		
		// All the variables can reach
		// Local variables
		Set<String> attrsBuff = ByteCodeUtil.getVariableNames(mn);
		// Fields 
		attrsBuff.addAll(ByteCodeUtil.getFieldNames(cn));
		
		if (attrs!=null){
			for (String varName:attrs) {			
			if (!(attrsBuff.contains(varName))) {
				continue;
			}
			il.add(new VarInsnNode(ALOAD, mapVariableIndex));
			il.add(new LdcInsnNode(varName));
			il.add(genLoadInsn( cn, mn, varName));
			il.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
			il.add(new InsnNode(POP));
			}
		}
		else{
			for (String varName:attrsBuff) {			
				il.add(new VarInsnNode(ALOAD, mapVariableIndex));
				il.add(new LdcInsnNode(varName));
				il.add(genLoadInsn( cn, mn, varName));
				il.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
				il.add(new InsnNode(POP));
				}
		}
		
		
		//il.add(new VarInsnNode(ALOAD, 0));
		//il.add(new InsnNode(ACONST_NULL));
		il.add(getInsnNodeOfThis(objectType));
		il.add(new VarInsnNode(ALOAD, mapVariableIndex));
		il.add(new LdcInsnNode(id));
		il.add(new MethodInsnNode(INVOKESTATIC, "edu/pku/innerTest/Controller", "handleCheckExpr", "(Ljava/lang/Object;Ljava/util/Map;I)V"));
		
		return il;
	}

	private InsnList genLoadInsn(ClassNode cn, MethodNode mn, String varName) {
		InsnList il = new InsnList();
		LocalVariableNode lvn= ByteCodeUtil.getLocalVariableByName(mn, varName);
		FieldNode field = ByteCodeUtil.getFieldNodeByName(cn, varName);
		VariableFactory vf = VariableFactory.getInstance();
		
		if (lvn != null) {
			il.add(vf.getVariable(lvn.desc).genLoadLocalInsnList(lvn.index));
		} else if (field != null) {
			il.add(vf.getVariable(field.desc).genLoadFieldInsnList(cn.name, field));
		} else {
		}
		
		return il;
	}
	
	/*
	 * Controller.handleCheckExpr(this, checkTmpMap, checkType);
	 * Controller.handleCheckExpr(null, checkTmpMap, checkType);
	 * 
	 * this for object
	 * null for class
	 */
	private AbstractInsnNode getInsnNodeOfThis(int objectType) {
		switch(objectType){
		case ActionKeys.CHECK_INSN_OBJ:
			return new VarInsnNode(ALOAD, 0);
		case ActionKeys.CHECK_INSN_CLASS:
			return new InsnNode(ACONST_NULL);
		default:
			return null;
		}
	}
}
