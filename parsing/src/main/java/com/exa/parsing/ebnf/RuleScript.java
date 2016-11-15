package com.exa.parsing.ebnf;

public class RuleScript {
	protected String name;
	protected String src;
	
	protected CompiledRule compiled = null;
	
	public RuleScript(String name, String src) {
		super();
		this.name = name;
		this.src = src;
	}
	
	public String name() { return name; }
	
	public String src() { return src; }
	
	public CompiledRule compiled() { return compiled; }
	public void compiled(CompiledRule compiled) { this.compiled = compiled; }
	
	@Override
	public String toString() { return src; }

}
