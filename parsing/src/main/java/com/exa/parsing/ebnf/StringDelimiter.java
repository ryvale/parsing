package com.exa.parsing.ebnf;

import com.exa.buffer.CharReader;
import com.exa.buffer.CharReader.ClientBuffer;
import com.exa.lexing.LexingRules;
import com.exa.lexing.WordSeparator;
import com.exa.utils.ManagedException;

public class StringDelimiter extends WordSeparator {

	public StringDelimiter(LexingRules lexer, Character delimiter) {
		super(delimiter.toString(), lexer);
	}

	@Override
	public boolean isFirstCharManager() { return true; }
	
	@Override
	public void nextToEndOfWord(CharReader script) throws ManagedException {
		ClientBuffer db = script.listen();
		
		String bd;
		do {
			script.nextToChar(keyword.charAt(0));
			bd = db.toString();
			
			if(bd.length()<2) break;
			
		} while(bd.charAt(bd.length()-2) == '\\');
		
		db.release();
	}

	@Override
	public void nextToEndOfExpression(CharReader script) throws ManagedException {
		nextToEndOfWord(script);
	}
	
}
