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
	private boolean separatorsPresent = false; 

	public PreExpMan(RulesConfig res) {
		super(null);
		this.res = res;
	}
	
	public PreExpMan() { this(new RulesConfig()); }

	@Override
	public ParsingEntity push(ParsingEntity currentPE, List<ParsingEvent> pevs) {
		
		for(ParsingEvent pev : pevs) {
			if(pev.isParent()) continue;
			String word = pev.getWord();
			
			if(word == null) continue;
			
			if(PreLanguage.WS_RULE_PART_SEP.equals(word)) continue;
			
			if(PreLanguage.WS_RULE_SEP.equals(word)) {
				if(ignorePresent) {
					ignorePresent = false;
					continue;
				}
				
				if(separatorsPresent) {
					separatorsPresent = false;
					continue;
				}
			}
			
			if("ignore".equals(word)) {
				ignorePresent = true;
				continue;
			}
			
			if("separators".equals(word)) {
				separatorsPresent = true;
				continue;
			}
			
			if(ignorePresent) {
				res.charsToIgnore(word.substring(1, word.length() - 1).replaceAll("\\\\'", "'"));
				continue;
			}
			
			if(separatorsPresent) {
				if(",".equals(word)) continue;
				
				res.separator(word.substring(1, word.length() - 1).replaceAll("\\\\'", "'"));
				continue;
			}
			
			if(currentKey == null) {
				if(res.containsRule(word)) return new PEFail("The rule '" + word+ "' defined twice");
				currentKey = word;
				continue;
			}
			
			if(PreLanguage.WS_RULE_SEP.equals(word)) {
				res.addRule(currentKey, new RuleScript(currentKey, sbValue.toString()));
				currentKey = null;
				sbValue.setLength(0);
				continue;
			} 
			
			sbValue.append(word);
			
		}
		
		return currentPE;
	}

	@Override
	public RulesConfig compute() { return res; }
	
	
	

}
