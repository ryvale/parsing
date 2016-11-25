package com.exa.parsing.ebnf.expressions;

public class ConstantInteger extends Constant<Integer> {

	public ConstantInteger(Integer value) {
		super(value);
	}

	@Override
	public Operand<Integer> asOPInteger() {	return this; }

	
}
