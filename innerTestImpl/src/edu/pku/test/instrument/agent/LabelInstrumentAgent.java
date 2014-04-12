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

import edu.pku.test.instrument.InstrumentorConfig;

/**
 * 
 * @author zhutao
 * 
 */

public class LabelInstrumentAgent extends InstrumentAgent {

	@Override
	public void instrument(ClassNode cn, InstrumentorConfig ic) {
		// TODO Auto-generated method stub
		int lineNumber = getLineNoByIDString(cn, ic.getLocationMark());
		ic.setLineNo(lineNumber);
		
		super._instrument(cn, ic);
	}
	
	
	private int getLineNoByIDString(ClassNode cn, String idString) {
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

}
