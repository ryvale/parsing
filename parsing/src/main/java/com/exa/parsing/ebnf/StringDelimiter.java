package com.exa.parsing.ebnf;

import com.exa.lexing.CharReader;
import com.exa.lexing.CharReader.DataBuffer;
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
		DataBuffer db = script.bufferize();
		
		String bd;
		do {
			script.nextToChar(keyword.charAt(0));
			bd = db.value();
			
			if(bd.length()<2) break;
			
		} while(bd.charAt(bd.length()-2) == '\\');
		
		script.releaseBuffer(db);
	}

	@Override
	public void nextToEndOfExpression(CharReader script) throws ManagedException {
		nextToEndOfWord(script);
	}
	
}
