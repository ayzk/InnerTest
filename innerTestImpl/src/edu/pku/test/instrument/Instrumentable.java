package edu.pku.test.instrument;

import org.objectweb.asm.tree.ClassNode;

/**
 * 
 * @author zhutao
 * 
 */

public interface Instrumentable {
	
	public void instrument(ClassNode cn, InstrumentorConfig ic);
	
	public void setCodeGenerateAgent(CodeGenerateAgent ca);

}
