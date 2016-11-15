package com.exa.parsing.ebnf.expressions;

import com.exa.expression.OperandMan;
import com.exa.expression.StackEvaluator;
import com.exa.parsing.PEAllWord;

public class OpdManWord extends OperandMan<Item<?>> {
	
	public OpdManWord(StackEvaluator<Item<?>> evaluator) {
		super(evaluator);
	}

	@Override
	public ConstantParsingEntity parsed(String word) {
		if("$".equals(word)) return new ConstantParsingEntity(new PEAllWord());
		
		return null;
	}

}
