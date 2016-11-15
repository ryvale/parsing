package com.exa.parsing.ebnf.expressions;

import com.exa.expression.ComputedItem;
import com.exa.expression.StackEvaluator;
import com.exa.expression.XPressionException;

public abstract class UnaryOp<T> extends OperatorBase<T> {

	public UnaryOp(String symbol, int priority) {
		super(symbol, priority, 1);
	}

	@Override
	public void resolve(StackEvaluator<Item<?>> eval, int order, int nbOperands) throws XPressionException {
		ComputedItem<Item<?>> ci = eval.popOperand();
		if(ci == null) throw new XPressionException("Error in expression near '"+symbol+"'");
		
		Item<?> item = ci.item();
		Operand<?> oprd = item.asOperand();
		if(oprd == null) {
			item.asOperator().resolve(eval, ci.order(), ci.asComputedOperator().nbOperand());
			oprd = eval.popOperand().item().asOperand();
		}
		
		Operand<T> specificOprd = getResult(oprd);
		
		eval.pushOperand(specificOprd);
	}
	
	protected abstract Operand<T> getResult(Operand<?> oprd) throws XPressionException;

	@Override
	public boolean isPostUnary() { return false; }

	@Override
	public boolean isPreUnary() { return true; }

}
