package com.exa.lexing;

import com.exa.utils.ManagedException;

public class WordWithOpenCloseDelimiter extends WordSeparator {

	protected Character closeDelimiter;
	
	public WordWithOpenCloseDelimiter(LexingRules lexer, Character openDelimiter, Character closeDelimiter) {
		super(openDelimiter.toString(), lexer);
		
		this.closeDelimiter = closeDelimiter;
	}
	
	@Override
	public void nextToEndOfWord(CharReader charReader) throws ManagedException {
		charReader.nextToChar(closeDelimiter);
	}
	
	@Override
	public void nextToEndOfExpression(CharReader charReader) throws ManagedException {
		nextToEndOfWord(charReader);
	}

	@Override
	public boolean isFirstCharManager() {
		return true;
	}
}
