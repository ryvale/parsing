package com.exa.parsing.ebnf.expressions;

import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedString;

public class FDString extends Field<String> {

	public FDString(String name) {
		super(name);
	}

	@Override
	public FDString asStringField() { return this; }

	@Override
	public FDString clone() { return new FDString(name); }

	@Override
	public ParsedString valueAsParsedObject() {
		return new ParsedString(value);
	}
	
}
