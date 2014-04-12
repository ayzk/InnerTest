package edu.pku.test.instrument.agent;

import org.objectweb.asm.tree.ClassNode;

import edu.pku.test.instrument.InstrumentorConfig;

/**
 * 
 * @author zhutao
 * 
 */

public class LineNumberInstrumentAgent extends InstrumentAgent {

	@Override
	public void instrument(ClassNode cn, InstrumentorConfig ic) {
		// TODO Auto-generated method stub
		super._instrument(cn, ic);
	}

}
