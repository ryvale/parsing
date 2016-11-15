package com.exa.parsing.ebnf.expressions;

import com.exa.expression.XPressionException;
import com.exa.parsing.ParsingEntity;

public interface Operand<T> extends com.exa.expression.Operand<Item<?>>, Item<T> {

	T value() throws XPressionException;

	Operand<T> asSpecificItem();

	Operand<ParsingEntity> asOPParsingEntity();

	Operand<String> asOPString();

	Operand<String> asOPIdentifier();
	
	ExpressionOperand asExpressionOperand();

}