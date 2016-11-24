package com.exa.parsing.ebnf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RulesConfig {
	protected Map<String, RuleScript> rules;
	protected String charsToIgnore;
	protected Set<String> separators;
	
	public RulesConfig(Map<String, RuleScript> rules, String charsToIgnore, Set<String> separators) {
		super();
		this.rules = rules;
		this.charsToIgnore = charsToIgnore;
		this.separators = separators;
	}
	
	public RulesConfig(String charsToIgnore) {
		this(new HashMap<String, RuleScript>(), charsToIgnore, new HashSet<String>());
	}
	
	public RulesConfig() {
		this(new HashMap<String, RuleScript>(), "", new HashSet<String>());
	}
	
	public void charsToIgnore(String chars) { this.charsToIgnore = chars;}
	public String charsToIgnore() { return this.charsToIgnore; }
	
	public void separator(String sep) { this.separators.add(sep); }
	public Set<String> separators() { return this.separators; }
	
	public boolean containsRule(String name) { return rules.containsKey(name); }
	
	public void addRule(String name, RuleScript rule) { rules.put(name, rule); }
	
	
	public RuleScript getRule(String name) { return rules.get(name); }
}
