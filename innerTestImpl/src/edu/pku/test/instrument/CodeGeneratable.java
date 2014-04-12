package edu.pku.test.instrument;

import org.objectweb.asm.tree.ClassNode;

/**
 * 
 * @author zhutao
 * 
 */

public interface CodeGeneratable {
	public CodeTemplate generate(ClassNode cn, InstrumentorConfig ic);
}
