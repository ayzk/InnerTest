package edu.pku.test.instrument;

/**
 * 
 * @author zhutao
 * 
 */

public class InstrumentTemplate {
	
	public InstrumentTemplate() {
		
	}
	
	public Instrumentable getInstrumentAgent(){
		return instrumentAgent;
	}
	
	public CodeGeneratable getCodeGenerator() {
		return codeGenerator;
	}
	
	public void setInstrumentAgent(Instrumentable instrumentAgent) {
		this.instrumentAgent = instrumentAgent;
	}

	public void setCodeGenerator(CodeGeneratable codeGenerator) {
		this.codeGenerator = codeGenerator;
	}

	private Instrumentable instrumentAgent;
	
	private CodeGeneratable codeGenerator;

}
