package com.exa.parsing.ebnf.expressions;

import com.exa.expression.StackEvaluator;

public abstract class OperatorSymbol  {
	public abstract String symbol();
	
	public abstract OperatorBase<?> operatorOf(StackEvaluator<Item<?>> eval);
	
}
