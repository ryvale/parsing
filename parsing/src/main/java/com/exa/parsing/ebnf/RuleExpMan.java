package com.exa.parsing.ebnf;

import java.util.List;

import com.exa.parsing.ExpMan;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.expressions.Evaluator;
import com.exa.utils.ManagedException;

public class RuleExpMan extends ExpMan<CompiledRule> {
	protected final Evaluator eval;
		
	public RuleExpMan(Parsing<CompiledRule> parsing, RuleParser ruleParser) {
		super(parsing);
		eval = new Evaluator(ruleParser);
	}
	
	@Override
	public ParsingEntity push(String exp, ParsingEntity currentPE, List<ParsingEvent> peEvents) throws ManagedException {
		eval.push(exp);
		
		return currentPE;
	}
	
	@Override
	public CompiledRule compute() throws ManagedException {
		eval.compute();
		return new CompiledRule(eval.compute().asSpecificItem().asOperand().asOPParsingEntity().value(), eval.getRuleParser().getRuleConfig().charsToIgnore(), eval.getFieldsParsingMap());
	}
	
	@Override
	public void reset() { eval.reset();	}
	
}
