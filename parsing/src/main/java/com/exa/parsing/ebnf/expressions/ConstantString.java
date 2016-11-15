package com.exa.parsing.ebnf.expressions;

public class ConstantString extends Constant<String> {

	public ConstantString(String value) {
		super(value);
	}

	@Override
	public Operand<String> asOPString() { return this; }
	
	

}
