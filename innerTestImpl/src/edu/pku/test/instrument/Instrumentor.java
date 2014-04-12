package edu.pku.test.instrument;

import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import edu.pku.test.instrument.agent.LabelInstrumentAgent;
import edu.pku.test.instrument.agent.LineNumberInstrumentAgent;
import edu.pku.test.instrument.agent.RelativeLineNumberInstrumentAgent;

/**
 * 
 * @author zhutao
 * 
 */

public class Instrumentor {
	
	
	public static byte[] instrument(byte[] tb, InstrumentorConfig ic) {
		//System.out.println("lineNo: " + ic.getLineNo());
		return preTransform(tb, ic);
	}
	

	public static byte[] preTransform(byte[] tb, InstrumentorConfig ic) {

		byte[] b = null;
		try {
			ClassNode cn = new ClassNode();
			ClassReader cr = new ClassReader(tb);
			cr.accept(cn, 0);
						
//			ClassWriter cw0 = new ClassWriter(0);
//			cn.accept(cw0);
//			b=cw0.toByteArray();
//			FileOutputStream writebyte0 = new FileOutputStream("before" + ic.getId() + ".class"); 
//			writebyte0.write(b);
//			writebyte0.close();

			InstrumentTemplate it = getInstrumentTemplate(ic);
			it.getInstrumentAgent().instrument(cn, ic);
			
			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			b = cw.toByteArray();
			 //Write down the byte code for view
//			FileOutputStream writebyte = new FileOutputStream("after" + ic.getId() + ".class"); 
//			writebyte.write(b);
//			writebyte.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	private static InstrumentTemplate getInstrumentTemplate(InstrumentorConfig ic) {
		InstrumentTemplate it = new InstrumentTemplate();
		switch (ic.getLocationType()) {
			case InstrumentorConfig.KEY_LINE_NO:
				it.setInstrumentAgent(new LineNumberInstrumentAgent());
				break;
			case InstrumentorConfig.KEY_RELATIVE_IN_METHOD:
				it.setInstrumentAgent(new RelativeLineNumberInstrumentAgent());
				break;
			case InstrumentorConfig.KEY_ID_STRING:
				it.setInstrumentAgent(new LabelInstrumentAgent());
				break;
		}
		
		it.getInstrumentAgent().setCodeGenerateAgent(new CodeGenerateAgent());
		return it;
	}
	
	public static void main(String[] args) throws IOException {
		ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader("edu/pku/test/YearCaculator");
		cr.accept(cn, 0);
		ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		byte[] b = cw.toByteArray();
		FileOutputStream writebyte = new FileOutputStream("YearCaculator.class");
		writebyte.write(b);
		writebyte.close();
	}

}
