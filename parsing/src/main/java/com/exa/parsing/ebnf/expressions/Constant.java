package com.exa.parsing.ebnf.expressions;

public abstract class Constant<T> extends OperandBase<T> {
	protected T value;
	
	public Constant(T value) {
		super();
		this.value = value;
	}
	
	@Override
	public boolean isConstant() { return true; }
	
	@Override
	public T value() { return value; }
	
}
