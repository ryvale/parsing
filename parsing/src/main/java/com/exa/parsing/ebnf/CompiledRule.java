package com.exa.parsing.ebnf;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.exa.lexing.Language;
import com.exa.lexing.LexingRules;
import com.exa.parsing.ParsingEntity;

public class CompiledRule {
	protected Language language;
	protected Map<ParsingEntity, FieldComputer<?>> fieldComputers;

	public CompiledRule(Language language, Map<ParsingEntity, FieldComputer<?>> fieldComputers) {
		super();
		this.language = language;
		this.fieldComputers = fieldComputers;
	}
	
	public CompiledRule(ParsingEntity parsingEntity, String ignoreChars, Map<ParsingEntity, FieldComputer<?>> fieldComputers) {
		this(new Language(new LexingRules(ignoreChars), new HashSet<String>(), parsingEntity), fieldComputers);
	}
	
	public CompiledRule(Language language) { this(language, new LinkedHashMap<ParsingEntity, FieldComputer<?>>());}
	
	public Language language() { return language; }
	
	public FieldComputer<?> getFieldComputer(ParsingEntity pe) { return fieldComputers.get(pe); }
	
	public Map<ParsingEntity, FieldComputer<?>> getFieldComputers() { return fieldComputers; }
	
	public Map<ParsingEntity, FieldComputer<?>> clonedFieldComputers() {
		Map<ParsingEntity, FieldComputer<?>> res = new LinkedHashMap<>();
		
		for(ParsingEntity pe : fieldComputers.keySet()) {
			res.put(pe, fieldComputers.get(pe).clone());
		}
		
		return res;
	}

}
