package com.exa.parsing.ebnf;

import java.util.ArrayList;
import java.util.List;

public class ParsedArray extends ParsedObject<List<ParsedObject<?>>> {
	
	public ParsedArray(List<ParsedObject<?>> value) {
		super(value);
	}
	
	public ParsedArray() { this(new ArrayList<ParsedObject<?>>()); }

	@Override
	public ParsedArray asParsedArray() { return this;	}
	
	@Override
	public ParsedObject<?> get(int i) { return value.get(i); }
	
}
