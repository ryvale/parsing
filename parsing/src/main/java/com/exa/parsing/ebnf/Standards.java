package com.exa.parsing.ebnf;

import com.exa.parsing.PERule;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.stdxpression.parsing.NumberAW;
import com.exa.utils.ManagedException;

public class Standards {
	public final static String WS_KEYWORD_DELIMITER = "'";
	public final static String IDENTIFIER_CHARS = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static boolean isIdentifier(String token) {
		
		if(token.length() == 0) return false;
		
		char firstChar = token.charAt(0);
		
		if(NumberAW.NUMBER_CHARS.indexOf(firstChar)>=0) return false;
		
		if(Standards.IDENTIFIER_CHARS.indexOf(firstChar)<0) return false;
		
		for(int i=1; i<token.length(); i++) {
			if(Standards.IDENTIFIER_CHARS.indexOf(token.charAt(i))<0) return false;
		}
		
		return true;
	}
	
	public static boolean isInteger(String token) {
		
		if(token.length() == 0) return false;
		
		for(int i=0; i<token.length(); i++) {
			if(NumberAW.NUMBER_CHARS.indexOf(token.charAt(i))<0) return false;
		}
		
		return true;
	}
	
	public final static PERule PER_LX_INDENTIFIER = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) throws ManagedException {
			
			String id = parsing.lexerWord();
			
		
			return isIdentifier(id);
		}
	};
	
	public final static PERule PER_LX_INTEGER = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) throws ManagedException {
			
			String id = parsing.lexerWord();
		
			return isInteger(id);
		}
	};
}
