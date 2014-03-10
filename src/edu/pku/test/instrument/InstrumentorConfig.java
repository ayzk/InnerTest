package edu.pku.test.instrument;

import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

/**
 * 
 * @author zhutao
 * 
 */

public class InstrumentorConfig {
	
	public final static int KEY_LINE_NO = 1;
	public final static int KEY_RELATIVE_IN_METHOD = 2;
	public final static int KEY_ID_STRING = 3;
	
	
	private Set<String> attrs;
	private int relativeLineNo = -1;
	private String methodName;
	private int lineNo = -1;
	private String locationMark;
	
	private int type = -1;
	private int objectType = -1;
	private int id = -1;
	
	private int locationType;
	
	public InstrumentorConfig(Set<String> attrs, int lineNo, int id, int type, int objectType) {
		this.attrs = attrs;
		this.lineNo = lineNo;
		this.id = id;
		this.type = type;
		this.objectType = objectType;
		this.locationType = KEY_LINE_NO;
	}
	
	public InstrumentorConfig(Set<String> attrs, String locationMark, int id, int type, int objectType) {
		this.attrs = attrs;
		this.locationMark = locationMark;
		this.id = id;
		this.type = type;
		this.objectType = objectType;
		this.locationType = KEY_ID_STRING;

	}
	
	public InstrumentorConfig(Set<String> attrs, String methodName, int relativeLineNo, int id, int type, int objectType) {
		this.attrs = attrs;
		this.methodName = methodName;
		this.relativeLineNo = relativeLineNo;
		this.id = id;
		this.type = type;
		this.objectType = objectType;
		this.locationType = KEY_RELATIVE_IN_METHOD;

	}

	public Set<String> getAttrs() {
		return attrs;
	}

	public void setAttrs(Set<String> attrs) {
		this.attrs = attrs;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getLocationMark() {
		return locationMark;
	}

	public void setLocationMark(String locationMark) {
		this.locationMark = locationMark;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLocationType() {
		return locationType;
	}

	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}

	public int getRelativeLineNo() {
		return relativeLineNo;
	}

	public void setRelativeLineNo(int relativeLineNo) {
		this.relativeLineNo = relativeLineNo;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
}
