package com.exa.parsing.ebnf.predefs;

import java.util.List;

import com.exa.parsing.ExpMan;
import com.exa.parsing.PEFail;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.RuleScript;
import com.exa.parsing.ebnf.RulesConfig;

public class PreExpMan extends ExpMan<RulesConfig> {
	protected RulesConfig res;
	private String currentKey = null;
	private StringBuilder sbValue = new StringBuilder();
	private boolean ignorePresent = false; 

	public PreExpMan(RulesConfig res) {
		super(null);
		this.res = res;
	}
	
	public PreExpMan() { this(new RulesConfig()); }

	@Override
	public ParsingEntity push(String exp, ParsingEntity currentPE, List<ParsingEvent> peEvents) {
		if(PreLanguage.WS_RULE_PART_SEP.equals(exp)) return currentPE;
		
		if(PreLanguage.WS_RULE_SEP.equals(exp)) {
			if(ignorePresent) {
				ignorePresent = false;
				return currentPE;
			}
		}
		
		if("ignore".equals(exp)) {
			ignorePresent = true;
			return currentPE;
		}
		
		if(ignorePresent) {
			res.charsToIgnore(exp.substring(1, exp.length() - 1).replaceAll("\\\\'", "'"));
			return currentPE;
		}
		
		if(currentKey == null) {
			if(res.containsRule(exp)) return new PEFail("The rule '" + exp+ "' defined twice");
			currentKey = exp;
			return currentPE;
		}
		
		if(PreLanguage.WS_RULE_SEP.equals(exp)) {
			res.addRule(currentKey, new RuleScript(currentKey, sbValue.toString())); //.put(currentKey, new RuleScript(currentKey, sbValue.toString()));
			currentKey = null;
			sbValue.setLength(0);
			return currentPE;
		} 
		
		sbValue.append(exp);
		
		return currentPE;
	}

	@Override
	public RulesConfig compute() { return res; }
	
	
	

}
