package com.exa.utils.buffers;

public class SharedString {
	protected CharSequence stringBuffer;
	protected int start, end;
	
	public SharedString(CharSequence stringBuffer, int start, int end) {
		super();
		this.stringBuffer = stringBuffer;
		this.start = start;
		this.end = end;
	}


	@Override
	public String toString() {
		return stringBuffer.subSequence(start, end).toString();
	}
	
	

}
