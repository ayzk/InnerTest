package edu.pku.test.instrument.agent;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import edu.pku.test.constants.ActionKeys;
import edu.pku.test.instrument.CodeGenerateAgent;
import edu.pku.test.instrument.Instrumentable;
import edu.pku.test.instrument.Instrumentor;
import edu.pku.test.instrument.InstrumentorConfig;

/**
 * 
 * @author zhutao
 * 
 */

public abstract class InstrumentAgent implements Instrumentable {
	
	private CodeGenerateAgent ca = null;

	@Override
	public void setCodeGenerateAgent(CodeGenerateAgent ca) {
		this.ca = ca;
	}

	public void _instrument(ClassNode cn, InstrumentorConfig ic) {
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
							
							CodeGenerateAgent ca = new CodeGenerateAgent();
							il = ca.GenerateInsnList(cn, mn, lnn, ic.getId(), ic.getObjectType(), ic.getAttrs());
								

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
	
}
