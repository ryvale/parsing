package com.exa.parsing.ebnf.expressions;

public class IdentifierOperand extends Constant<String> {

	public IdentifierOperand(String value) {
		super(value);
	}

	@Override
	public Operand<String> asOPIdentifier() { return this; }
	
	

}
