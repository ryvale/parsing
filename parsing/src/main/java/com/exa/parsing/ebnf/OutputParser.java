package com.exa.parsing.ebnf;

import com.exa.parsing.IParser;
import com.exa.utils.ManagedException;

public class OutputParser implements IParser<ParsedObject<?>> {
	protected CompiledRule compiledRule;
	/*protected String rootRule;
	protected RuleParser ruleParser;
	
	public OutputParser(RuleParser ruleParser, String rootRule) {
		super();
		this.ruleParser = ruleParser;
		this.rootRule = rootRule;
	}
	
	public OutputParser(String ebnfScript, String rootRule) throws ManagedException {
		this(new RuleParser(new PreParser().parse(ebnfScript)), rootRule);
	}*/
	
	
	public OutputParser(CompiledRule compiledRule) {
		super();
		this.compiledRule = compiledRule;
	}

	@Override
	public ParsedObject<?> parse(String str) throws ManagedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public boolean validates(String exp) throws ManagedException {
		// TODO Auto-generated method stub
		return false;
	}
	

}
