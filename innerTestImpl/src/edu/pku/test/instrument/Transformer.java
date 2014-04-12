package edu.pku.test.instrument;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
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
import edu.pku.test.instrument.variables.Variable;
import edu.pku.test.instrument.variables.VariableFactory;

public class Transformer {
	
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
	
	public static void transformWithLineNo(ClassNode cn, InstrumentorConfig ic) {
		if ((cn.access & ACC_INTERFACE) == 0) {
			for (MethodNode mn : (List<MethodNode>) cn.methods) {
				if ("<init>".compareTo(mn.name) == 0
						|| "<clinit>".compareTo(mn.name) == 0) {
					continue;
				}
				
				int line = -1;
				boolean finded = false;
				boolean instrumented = false;
				InsnList insns = mn.instructions;
				Iterator<AbstractInsnNode> it = insns.iterator();
				while (it.hasNext()) {
					AbstractInsnNode in = it.next();
					
					if (in.getType() == AbstractInsnNode.LINE) {
						
						LineNumberNode lnn = (LineNumberNode) in;
						line = lnn.line;
						System.out.println(Instrumentor.class.getName() + " - [INFO]: check line - " + line);
						if (!finded && line == ic.getLineNo()) {
							finded = true;
							System.out.println("[DBUG] " + Instrumentor.class + " - Find line: "+ line);
						} else if (finded && !instrumented) {
							InsnList il;
							
								il = GenerateInsnListForExpression(cn, mn, lnn, ic.getAttrs(), ic.getId(), ic.getObjectType());

							insns.insert(lnn.start, il);
							System.out.println(Instrumentor.class.getName() + " - [INFO] - LineInstrumented: "+ line);
							mn.maxStack += 4;
							mn.maxLocals += 4;
							instrumented = true;
						}
					} 
				}
			}

		}
	}
	
	/*
	 * Location type is user defined id string
	 */
	public static void tranformWithIDString(ClassNode cn, InstrumentorConfig ic) {
		// TODO 
		int lineNo = getLineNoByIDString(cn, ic.getLocationMark());
		ic.setLineNo(lineNo);
		transformWithLineNo(cn, ic);
	}
	
	/*
	 * Location type is relative line number in method
	 */
	public static void transformRelativeInMethod(ClassNode cn, InstrumentorConfig ic) {
		int lineNo = getLineNoByRelativeInMethod(cn, ic.getMethodName(), ic.getRelativeLineNo());
		
		ic.setLineNo(lineNo);
		transformWithLineNo(cn, ic);
	}
	
	private static int getLineNoByRelativeInMethod(ClassNode cn, String methodName, int relativeLineNo) {
		int line;
		if ((cn.access & ACC_INTERFACE) == 0) {
			for (MethodNode mn : (List<MethodNode>) cn.methods) {
				if (methodName.compareTo(mn.name) != 0) {
					continue;
				}
				InsnList insns = mn.instructions;
				Iterator<AbstractInsnNode> it = insns.iterator();
				while (it.hasNext()) {
					AbstractInsnNode in = it.next();
					
					if (in.getType() == AbstractInsnNode.LINE) {
						LineNumberNode lnn = (LineNumberNode) in;
						line = lnn.line;
						return line + relativeLineNo - 1;
					} 
				}
			}
		}
		return -1;
	}
	
	private static int getLineNoByIDString(ClassNode cn, String idString) {
		int line = -1;
		if ((cn.access & ACC_INTERFACE) == 0) {
			for (MethodNode mn : (List<MethodNode>) cn.methods) {
				if ("<init>".compareTo(mn.name) == 0
						|| "<clinit>".compareTo(mn.name) == 0) {
					continue;
				}
				
				InsnList insns = mn.instructions;
				Iterator<AbstractInsnNode> it = insns.iterator();
				while (it.hasNext()) {
					AbstractInsnNode in = it.next();
					
					if (in.getType() == AbstractInsnNode.LINE) {
						LineNumberNode lnn = (LineNumberNode) in;
						line = lnn.line;
					} else if (in.getType() == AbstractInsnNode.LDC_INSN) {
						LdcInsnNode li = (LdcInsnNode) in;
						if (li.cst instanceof String) {
							String ldcStr = (String)li.cst;
							if (ldcStr.compareTo(idString) == 0) {
								return line;
							}
						}
					}
				}
			}

		}
		return -1;
	}
	
	/*
	 * Generate the instructions:
	 * 
	 * 	HashMap<String, Object> checkTmpMap = new HashMap<String, Object>();
	 * 	checkTmpMap.put(varName, varValue);
	 * 	...
	 * 	Controller.handleCheckExpr(this, checkTmpMap, checkType);
	 * 
	 */
	private static InsnList GenerateInsnListForExpression(ClassNode cn, MethodNode mn, LineNumberNode lnn, Set<String> attrs, int checkerId, int objectType) {
		// new temp hashmap
		InsnList il = new InsnList();
		
		LabelNode last = ByteCodeUtil.getLastLabelNode(mn);
		LocalVariableNode localVariable = ByteCodeUtil.getLastLocalVariable(mn);
				
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
		
		for (String varName:attrs) {
			il.add(new VarInsnNode(ALOAD, mapVariableIndex));
			il.add(new LdcInsnNode(varName));
			insertLoadInsn(il, cn, mn, varName);
			il.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
			il.add(new InsnNode(POP));
		}
		
		//il.add(new VarInsnNode(ALOAD, 0));
		//il.add(new InsnNode(ACONST_NULL));
		il.add(getInsnNodeOfThis(objectType));
		il.add(new VarInsnNode(ALOAD, mapVariableIndex));
		il.add(new LdcInsnNode(checkerId));
		il.add(new MethodInsnNode(INVOKESTATIC, "edu/pku/test/Controller", "handleCheckExpr", "(Ljava/lang/Object;Ljava/util/Map;I)V"));
		
		return il;
	}
	/*
	private static InsnList GenerateInsnListForGeneral(ClassNode cn, MethodNode mn, LineNumberNode lnn, int id, int objectType) {
		InsnList il = new InsnList();
		
		LabelNode last = ByteCodeUtil.getLastLabelNode(mn);
		LocalVariableNode localVariable = ByteCodeUtil.getLastLocalVariable(mn);
		
		int indexStep;
		if (typeMap.containsKey(localVariable.desc)) {
			indexStep = localVariable.index + typeMap.get(localVariable.desc).indexStep;
		}  else {
			indexStep = localVariable.index + 1;
		}
		
		LocalVariableNode tmpMapNode = new LocalVariableNode("checkTmpMap","Ljava/util/HashMap;","Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/Object;>;",lnn.start, last, indexStep);
		mn.localVariables.add(tmpMapNode);
		
		il.add(new TypeInsnNode(NEW, "java/util/HashMap"));
		il.add(new InsnNode(DUP));
		il.add(new MethodInsnNode(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V"));
		
		int mapVariableIndex = ByteCodeUtil.getLocalVariableByName(mn, "checkTmpMap").index;
		il.add(new VarInsnNode(ASTORE, mapVariableIndex));
		
		// All the variables can reach
		// Local variables
		Set<String> attrs = ByteCodeUtil.getVariableNames(mn);
		// Fields 
		attrs.addAll(ByteCodeUtil.getFieldNames(cn));
		
		for (String varName:attrs) {
			il.add(new VarInsnNode(ALOAD, mapVariableIndex));
			il.add(new LdcInsnNode(varName));
			insertLoadInsn(il, cn, mn, varName);
			il.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
			il.add(new InsnNode(POP));
		}
		
		il.add(getInsnNodeOfThis(objectType));
		il.add(new VarInsnNode(ALOAD, mapVariableIndex));
		il.add(new LdcInsnNode(id));
		il.add(new MethodInsnNode(INVOKESTATIC, "edu/pku/test/Controller", "handleCheckExpr", "(Ljava/lang/Object;Ljava/util/Map;I)V"));
		
		return il;
	}
	*/
	/*
	private static void insertLoadInsn(InsnList il, ClassNode cn, MethodNode mn, String varName) {
		LocalVariableNode lvn= ByteCodeUtil.getLocalVariableByName(mn, varName);
		FieldNode field = ByteCodeUtil.getFieldNodeByName(cn, varName);
		if (lvn != null) {
			if (typeMap.containsKey(lvn.desc)) {
				InstructionType it = typeMap.get(lvn.desc);
				il.add(new VarInsnNode(it.loadCommand, lvn.index));
				il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/" + it.type, "valueOf", "("+ lvn.desc + ")Ljava/lang/"+it.type+";"));
			} else {
				il.add(new VarInsnNode(ALOAD, lvn.index));
			}
		} else if (field != null) {
			if ((field.access & ACC_STATIC) == 0) {
				il.add(new VarInsnNode(ALOAD, 0));
				il.add(new FieldInsnNode(GETFIELD, cn.name, field.name,
						field.desc));
			} else {
				il.add(new FieldInsnNode(GETSTATIC, cn.name, field.name,
						field.desc));
			}
			if (typeMap.containsKey(field.desc)) {
				InstructionType it = typeMap.get(field.desc);
				il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/" + it.type, "valueOf", "("+ field.desc + ")Ljava/lang/"+it.type+";"));
			}
		} else {
		}
	}
	*/

	private static void insertLoadInsn(InsnList il, ClassNode cn, MethodNode mn, String varName) {
		LocalVariableNode lvn= ByteCodeUtil.getLocalVariableByName(mn, varName);
		FieldNode field = ByteCodeUtil.getFieldNodeByName(cn, varName);
		VariableFactory vf = VariableFactory.getInstance();
		
		if (lvn != null) {
			il.add(vf.getVariable(lvn.desc).genLoadLocalInsnList(lvn.index));
		} else if (field != null) {
			il.add(vf.getVariable(field.desc).genLoadFieldInsnList(cn.name, field));
		} else {
		}
	}
	
	/*
	 * Controller.handleCheckExpr(this, checkTmpMap, checkType);
	 * Controller.handleCheckExpr(null, checkTmpMap, checkType);
	 * 
	 * this for object
	 * null for class
	 */
	private static AbstractInsnNode getInsnNodeOfThis(int objectType) {
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
