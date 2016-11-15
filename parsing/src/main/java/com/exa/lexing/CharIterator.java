package com.exa.lexing;

import com.exa.utils.ManagedException;

public interface CharIterator {
	void open() throws ManagedException;
	void close() throws ManagedException;
	boolean next() throws ManagedException;
	void skip(long n) throws ManagedException;
	//void setPosition(long n) throws ManagedException;
	long getPosition();
	char getCurrentChar();
	
	boolean eof();
	
	int lineNumber();
	int getColPositionInLine();
	
	void reset() throws ManagedException;
	
	void reset(long startPosition) throws ManagedException;
	
	CharIterator cloneIterator() throws ManagedException;
	
}