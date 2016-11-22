package com.exa.parsing.ebnf;

import com.exa.lexing.Language;
import com.exa.lexing.WordIterator;
import com.exa.parsing.ExpMan;
import com.exa.parsing.Parser;
import com.exa.parsing.ParsingEntity;
import com.exa.utils.ManagedException;

public class RuleParser extends Parser<CompiledRule> {
	protected RulesConfig rulesConfig;

	public RuleParser(RulesConfig rulesConfig, Language language, boolean debugOn) {
		super(language, debugOn);
		this.rulesConfig = rulesConfig;
	}
	
	public RuleParser(RulesConfig rulesConfig) { this(rulesConfig, new RuleLanguage(), false); }
	
	public RuleParser(RulesConfig rulesConfig, boolean debugOn) { this(rulesConfig, new RuleLanguage(), debugOn); }
	
	public  void parseRule(String ruleName) throws ManagedException { 
		RuleScript rs = rulesConfig.getRule(ruleName);
		
		if(rs == null) throw new ManagedException("Unknown rule '"+ruleName+"'");
		
		if(rs.compiled() == null) rs.compiled(parse(rs.src()));	
	}
	
	@Override
	public ExpMan<CompiledRule> createExpMan(WordIterator wi) throws ManagedException {
		return new RuleExpMan(null, this);
	}
	
	public RulesConfig getRuleConfig() { return rulesConfig; }

	@Override
	public boolean listen(ParsingEntity pe) { return true; }
	
	
	
}
