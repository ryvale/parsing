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
	public ParsingEntity push(ParsingEntity currentPE, List<ParsingEvent> pevs) throws ManagedException {
		
		for(ParsingEvent pev : pevs) {
			if(pev.isParent()) continue;
			String word = pev.getWord();
			if(word == null) continue;
			
			eval.push(word);
		}
		
		return currentPE;
	}
	
	@Override
	public CompiledRule compute() throws ManagedException {
		eval.compute();
		return new CompiledRule(eval.compute().asOPParsingEntity().value(), eval.getRuleParser().getRuleConfig().charsToIgnore(), eval.getFields(), eval.getFieldsParsingMap());
	}
	
	@Override
	public void reset() { eval.reset();	}
	
}
