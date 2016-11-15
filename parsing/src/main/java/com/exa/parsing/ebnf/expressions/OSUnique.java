package com.exa.parsing.ebnf.expressions;

import com.exa.expression.StackEvaluator;

public class OSUnique<T> extends OperatorSymbol {
	protected OperatorBase<T> op;
	
	public OSUnique(OperatorBase<T> op) {
		super();
		this.op = op;
	}

	@Override
	public String symbol() { return op.symbol(); }

	@Override
	public OperatorBase<T> operatorOf(StackEvaluator<Item<?>> eval) {
		return op;
	}

}
