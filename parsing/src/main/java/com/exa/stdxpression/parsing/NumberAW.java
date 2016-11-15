package com.exa.stdxpression.parsing;

import com.exa.lexing.ActiveWord;
import com.exa.lexing.CharReader;
import com.exa.lexing.LexingRules;
import com.exa.utils.ManagedException;

public class NumberAW extends ActiveWord {
	public static final String NUMBER_CHARS = "0123456789";
	protected static final String NUMBER_TYPE_TERMINATION = "fd";
	public static final String WS_DECIMAL_SEP = ".";
	
	protected boolean pointMet;
	
	public NumberAW(LexingRules lexer, boolean pointMet) {	
		super("", lexer); 
		this.pointMet = pointMet;
	}
	
	@Override
	public boolean isFirstCharManager() { return true; }

	@Override
	public void nextToEndOfWord(CharReader script) throws ManagedException {
		Character lastCharacter = script.nextWhileCharIn(NUMBER_CHARS);
		
		if(lastCharacter == null) return;
		
		if(NUMBER_TYPE_TERMINATION.indexOf(lastCharacter.toString().toLowerCase()) >= 0) {
			script.nextChar();
			return;
		}
		
		if(pointMet) return;
		
		if(lastCharacter.toString().equals(WS_DECIMAL_SEP)) {
			String t = script.nextForwardChar(2);
			if(NUMBER_CHARS.indexOf(t.substring(1))<0) return;
			script.nextChar();
			lastCharacter = script.nextWhileCharIn(NUMBER_CHARS);
		}
		
		if(lastCharacter == null) return;
		
		if(NUMBER_TYPE_TERMINATION.indexOf(lastCharacter.toString().toLowerCase()) >= 0) {
			script.nextChar();
			return;
		}
	}

	@Override
	public void nextToEndOfExpression(CharReader script)	throws ManagedException {
		nextToEndOfWord(script);
	}
	
}
