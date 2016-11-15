package com.exa.parsing.ebnf.expressions;

public abstract class OperatorBase<T> extends com.exa.expression.OperatorBase<Item<?>> implements Item<T>, Operator<T> {
	
	public OperatorBase(String symbol, int priority, int nbOperands) {
		super(symbol, priority, nbOperands);
	}
	
	@Override
	public Operator<T> asOperator() { return this;	}
	
	@Override
	public Operator<T> asSpecificItem() { return this; }
	
	@Override
	public Operand<T> asOperand() { return null; }
	
}
