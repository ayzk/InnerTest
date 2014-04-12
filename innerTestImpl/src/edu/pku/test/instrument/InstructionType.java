package edu.pku.test.instrument;

/**
 * 
 * @author zhutao
 * 
 */

public class InstructionType {
	public String desc;
	public String type;
	public int loadCommand;
	public int indexStep;
	
	public InstructionType (String desc, String type, int loadCommand, int indexStep) {
		this.desc = desc;
		this.type = type;
		this.loadCommand = loadCommand;
		this.indexStep = indexStep;
	}

}
