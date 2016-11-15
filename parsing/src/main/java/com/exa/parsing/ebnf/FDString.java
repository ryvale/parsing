package com.exa.parsing.ebnf;

public class FDString extends Field<String> {

	public FDString(String name) {
		super(name);
	}

	@Override
	public Field<String> asStringField() { return this; }

	@Override
	public Field<String> clone() {
		return new FDString(name);
	}
	
	

}
