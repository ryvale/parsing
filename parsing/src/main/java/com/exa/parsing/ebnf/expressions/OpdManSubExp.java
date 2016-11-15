package com.exa.parsing.ebnf.expressions;

import com.exa.expression.StackEvaluator;
import com.exa.expression.SubExpOperandMan;

public class OpdManSubExp extends SubExpOperandMan<Item<?>> {

	public OpdManSubExp(StackEvaluator<Item<?>> evaluator, String subExpSymbol, String subExpResolutionSymbol) {
		super(evaluator, subExpSymbol, subExpResolutionSymbol);
	}

	@Override
	protected ExpressionOperand createExpressionOperand() {
		return new ExpressionOperand(evaluator);
	}
	
}
