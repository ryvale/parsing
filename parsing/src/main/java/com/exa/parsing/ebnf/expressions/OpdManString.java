package com.exa.parsing.ebnf.expressions;

import com.exa.expression.OperandMan;
import com.exa.expression.StackEvaluator;

public class OpdManString extends OperandMan<Item<?>> {
	
	public OpdManString(StackEvaluator<Item<?>> evaluator) {
		super(evaluator);
	}
	
	@Override
	public ConstantString parsed(String word) {
		if(word.startsWith("'")) return new ConstantString(word.substring(1, word.length() - 1).replaceAll("\\\\'", "'"));
		
		return null;
	}
	
	
}
