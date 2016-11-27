package com.exa.lexing;

import com.exa.utils.ManagedException;

public class EscapeCharMan {
	public static final EscapeCharMan NO_ESCAPE = new EscapeCharMan();
	
	public static class Standard extends EscapeCharMan {
		
		public Standard(CharIterator charReader) {
			super(charReader);
		}
		
		public Standard() {
			this(null);
		}

		@Override
		public char translated(char c) throws ManagedException {
			if(c == '\\') {
				if(charIterator.next()) {
				
					char nextChar = charIterator.getCurrentChar();
					if(nextChar == 'n') return '\n';
					if(nextChar == 'r') return '\r';
					if(nextChar == 't') return '\t';
					if(nextChar == '\\') return '\\';
				}
				throw new ManagedException("EOF met while expecting a char before escape character.");
				
			}
			return c;
		}
	}
	
	protected CharIterator charIterator;
	
	public EscapeCharMan(CharIterator charIterator) {
		super();
		this.charIterator = charIterator;
	}
	
	public EscapeCharMan() { this(null); }

	public char translated(char c) throws ManagedException { return c; }

	public CharIterator getCharIterator() {
		return charIterator;
	}

	public void setCharIterator(CharIterator charReader) {
		this.charIterator = charReader;
	}

}
