package com.exa.parsing.ebnf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.exa.parsing.ParsingEntity;
import com.exa.utils.ManagedException;

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
	
	public RulesConfig charsToIgnore(String chars) { this.charsToIgnore = chars; return this; }
	public String charsToIgnore() { return this.charsToIgnore; }
	
	public RulesConfig separator(String sep) { this.separators.add(sep); return this; }
	public Set<String> separators() { return this.separators; }
	
	public boolean containsRule(String name) { return rules.containsKey(name); }
	
	public RulesConfig addRule(String name, RuleScript rule) throws ManagedException { 
		if(rules.containsKey(name)) throw new ManagedException("The rule '"+name+"' already exists.");
		rules.put(name, rule); return this;
	}
	
	public RulesConfig addPredefinedRule(String name, ParsingEntity parsingEntity) throws ManagedException {
		return addRule(name, new RSPredefined(name, parsingEntity));
	}
	
	public RuleScript getRule(String name) { return rules.get(name); }
	
	
}
