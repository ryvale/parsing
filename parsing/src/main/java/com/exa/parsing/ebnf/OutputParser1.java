package com.exa.parsing.ebnf;

import java.util.Map;

import com.exa.lexing.WordIterator;
import com.exa.parsing.ExpMan;
import com.exa.parsing.Parser;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.expressions.FieldComputer;
import com.exa.utils.ManagedException;

public class OutputParser1 extends Parser<ParsedMap> {
	protected CompiledRule compiledRule;

	public OutputParser1(CompiledRule compiledRule, boolean debugOn) {
		super(compiledRule.language(), debugOn);
		this.compiledRule = compiledRule;
		
		Map<ParsingEntity, FieldComputer<?>> fieldComputers = compiledRule.getFieldComputers();
		for(ParsingEntity pe : fieldComputers.keySet()) {
			pesToListen.add(pe);
		}
		
	}
	
	public OutputParser1(CompiledRule compiledRule) { this(compiledRule, false); }

	@Override
	public ExpMan<ParsedMap> createExpMan(WordIterator wi) throws ManagedException {
		return new OutputExpMan(null, compiledRule);
	}

}
