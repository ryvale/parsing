package com.exa.parsing.ebnf;

public class ParsedString extends ParsedObject<String> {

	public ParsedString(String value) {
		super(value);
	}

	@Override
	public ParsedString asParsedString() { return this; }

	@Override
	public String toString() { return value; }
	
}
