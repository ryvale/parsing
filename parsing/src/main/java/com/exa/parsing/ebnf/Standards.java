package com.exa.parsing.ebnf;

import com.exa.stdxpression.parsing.NumberAW;

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
}
