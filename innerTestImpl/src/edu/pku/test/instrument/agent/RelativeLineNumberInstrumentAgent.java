package edu.pku.test.instrument.agent;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import edu.pku.test.instrument.InstrumentorConfig;

/**
 * 
 * @author zhutao
 * 
 */

public class RelativeLineNumberInstrumentAgent extends InstrumentAgent {
	
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("static-access")
	@Override
	public void instrument(ClassNode cn, InstrumentorConfig ic) {
		// TODO Auto-generated method stub
		int lineNumber = this.getLineNoByRelativeInMethod(cn, ic.getMethodName(), ic.getRelativeLineNo());
		ic.setLineNo(lineNumber);
		
		super._instrument(cn, ic);
	}
}
