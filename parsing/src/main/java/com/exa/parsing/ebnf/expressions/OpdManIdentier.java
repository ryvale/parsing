package com.exa.parsing.ebnf.expressions;

import com.exa.expression.OperandMan;
import com.exa.expression.StackEvaluator;
import com.exa.parsing.ebnf.Standards;

public class OpdManIdentier extends OperandMan<Item<?>> {

	public OpdManIdentier(StackEvaluator<Item<?>> evaluator) {
		super(evaluator);
	}

	@Override
	public IdentifierOperand parsed(String word) {
		if(Standards.isIdentifier(word)) return new IdentifierOperand(word);
		
		return null;
	}

}
