package com.exa.lexing;

import com.exa.utils.ManagedException;

public class LimitedCharIterator implements CharIterator {
	protected CharIterator charIterator;
	protected long startPosition, endPosition;
	
	public LimitedCharIterator(CharIterator charIterator, long startPosition, long endPosition) throws ManagedException {
		if(startPosition<0) throw new ManagedException("The start position should not be lesser than 0.");
		
		if(endPosition<startPosition) throw new ManagedException("The start position should not be greater than the end position.");
		
		this.startPosition = startPosition;
		charIterator.reset(startPosition);
		this.endPosition = endPosition;
		this.charIterator = charIterator;
	}

	@Override
	public boolean next() throws ManagedException {
		if(charIterator.getPosition()<endPosition) return charIterator.next(); 
		return false;
	}

	@Override
	public void skip(long n) throws ManagedException {
		if(charIterator.getPosition()+n>endPosition) throw new ManagedException("The position " + charIterator.getPosition() + n + " is beyond the the limit of the iterator.");
		
		charIterator.skip(n);
	}

	@Override
	public long getPosition() {	return charIterator.getPosition(); }

	@Override
	public char getCurrentChar() { return charIterator.getCurrentChar(); }

	@Override
	public int lineNumber() { return charIterator.lineNumber();	}

	@Override
	public int getColPositionInLine() {	return charIterator.getColPositionInLine();	}

	@Override
	public void reset() throws ManagedException {
		charIterator.reset();
	}

	@Override
	public void reset(long startPosition) throws ManagedException {
		charIterator.reset(startPosition);
	}

	@Override
	public boolean eof() {
		return charIterator.getPosition() >= endPosition;
	}
	
	@Override
	public LimitedCharIterator cloneIterator() throws ManagedException {
		
		return new LimitedCharIterator(charIterator, startPosition, endPosition);
		
	}

	@Override
	public void open() throws ManagedException {
		charIterator.open();
	}

	@Override
	public void close() throws ManagedException {
		charIterator.close();
	}
}
