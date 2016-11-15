package com.exa.lexing;

import com.exa.utils.ManagedException;

public class StringCharIterator implements CharIterator {
	protected String string;
	protected int startPosition, endPosition;
	
	protected int lineNumber = 0, colPosInLine = 0;
	protected int currentPosition = -1;
	
	protected int bufferizeInc = 0;
	protected int registerPosition = -1;
	
	public StringCharIterator(String string, int startPosition, int endPosition, boolean autoOpen) throws ManagedException {
		if(startPosition<0) throw new ManagedException("The start position should not be lesser than 0.");
		//if(endPosition<startPosition) throw new ManagedException("The start position should not be greater than the end position.");
		
		this.string = string;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		
		this.currentPosition = startPosition - 1;
		
		if(autoOpen) open();
	}
	
	public StringCharIterator(String string, int startPosition, int endPosition) throws ManagedException {
		this(string, startPosition, endPosition, true);
	}

	public StringCharIterator(String exp, int p0) throws ManagedException {
		this(exp, p0, exp.length()-1, true);
	}
	
	public StringCharIterator(String exp) throws ManagedException {
		this(exp, 0, exp.length()-1, true);
	}


	@Override
	public boolean next() throws ManagedException {
		if(currentPosition>=string.length()-1) return false;
		
		if(currentPosition<0) currentPosition++;
		else {
			char lastChar = string.charAt(currentPosition);
			updateLineProperties(lastChar, string.charAt(++currentPosition));
		}
		
		return true;
	}

	private void updateLineProperties(char lastChar, char currentChar) {
		if(lastChar=='\r' && currentChar=='\n') return;
		
		else if(currentChar=='\n' || currentChar=='\r') {
			lineNumber++;
			colPosInLine = 0;
		}
		else colPosInLine++;
	}
	
	@Override
	public void skip(long n) throws ManagedException {
		for(int i=0;i<n;i++) {
			if(!next()) throw new ManagedException("The position " + currentPosition+n + " is beyond the the limit of the iterator.");
		}
	}

	@Override
	public long getPosition() {	return currentPosition;	}

	@Override
	public char getCurrentChar() {
		return string.charAt(currentPosition);
	}

	@Override
	public int lineNumber() { return lineNumber; }

	@Override
	public int getColPositionInLine() {	return colPosInLine; }

	@Override
	public void reset() throws ManagedException {
		lineNumber = 0; colPosInLine = 0;
		
		skip(startPosition);
	}

	@Override
	public void reset(long startPosition) throws ManagedException {
		if(startPosition<0) throw new ManagedException("The start position should not be lesser than 0.");
		
		this.startPosition = (int)startPosition;
		
		reset();
	}

	@Override
	public boolean eof() {
		return currentPosition>=string.length();
	}
	
	@Override
	public StringCharIterator cloneIterator() throws ManagedException {
		return new StringCharIterator(string, startPosition, endPosition, true);
	}

	@Override
	public void open() throws ManagedException {
		reset();
	}

	@Override
	public void close() throws ManagedException {
		
	}

}
