package com.exa.parsing.ebnf.expressions;

import com.exa.parsing.ParsingEntity;

public abstract class OperandBase<T> extends com.exa.expression.OperandBase<Item<?>> implements Operand<T> {
	
	@Override
	public Operand<T> asOperand() { return this; }
	
	@Override
	public OperatorBase<T> asOperator() { return null; }
	
	@Override
	public Operand<T> asSpecificItem() { return this; }
	
	@Override
	public Operand<ParsingEntity> asOPParsingEntity() { return null; }
	
	@Override
	public Operand<String> asOPString() { return null; }
	
	@Override
	public Operand<String> asOPIdentifier() { return null; }

	@Override
	public ExpressionOperand asExpressionOperand() { return null; }
	
	
	
}
