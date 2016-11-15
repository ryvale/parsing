package com.exa.parsing.ebnf.expressions;

import com.exa.expression.XPItem;

public interface Item<T> extends XPItem<Item<?>> {

	@Override
	Operand<T> asOperand();

	@Override
	Operator<T> asOperator();
	
}
