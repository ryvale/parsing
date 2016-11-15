package com.exa.parsing.ebnf;

import java.util.HashMap;
import java.util.Map;

public class RulesConfig {
	protected Map<String, RuleScript> rules;
	protected String charsToIgnore;
	
	public RulesConfig(Map<String, RuleScript> rules, String charsToIgnore) {
		super();
		this.rules = rules;
		this.charsToIgnore = charsToIgnore;
	}
	
	public RulesConfig(String charsToIgnore) {
		this(new HashMap<String, RuleScript>(), charsToIgnore);
	}
	
	public RulesConfig() {
		this(new HashMap<String, RuleScript>(), "");
	}
	
	public void charsToIgnore(String chars) { this.charsToIgnore = chars;}
	public String charsToIgnore() { return this.charsToIgnore; }
	
	public boolean containsRule(String name) { return rules.containsKey(name); }
	
	public void addRule(String name, RuleScript rule) { rules.put(name, rule); }
	
	
	public RuleScript getRule(String name) { return rules.get(name); }
}
