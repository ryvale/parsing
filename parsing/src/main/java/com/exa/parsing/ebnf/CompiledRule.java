package com.exa.parsing.ebnf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.exa.lexing.Language;
import com.exa.lexing.LexingRules;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.expressions.FieldComputer;

public class CompiledRule implements Cloneable {
	protected Language language;
	protected Map<ParsingEntity, FieldComputer<?>> fieldComputers;
	protected Map<String, Field<?>> fields;

	public CompiledRule(Language language, Map<String, Field<?>> fields, Map<ParsingEntity, FieldComputer<?>> fieldComputers) {
		super();
		this.language = language;
		this.fieldComputers = fieldComputers;
		this.fields = fields;
	}
	
	public CompiledRule(ParsingEntity parsingEntity, String ignoreChars, Set<String> separators, Map<String, Field<?>> fields, Map<ParsingEntity, FieldComputer<?>> fieldComputers) {
		this(new Language(new LexingRules(ignoreChars), new HashSet<String>(), parsingEntity), fields, fieldComputers);
		
		for (String sep : separators) {
			language.getLexingRules().addWordSeparator(sep);
		}
	}
	
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

	@Override
	public CompiledRule clone() {
		
		Map<String, Field<?>> nfields = new HashMap<>();
		for(String fname : fields.keySet()) {
			nfields.put(fname, fields.get(fname).clone());
		}
		
		Map<FieldComputer<?>, FieldComputer<?>> oldNewFC = new HashMap<>();
		
		Map<ParsingEntity, FieldComputer<?>> nfieldComputers = new HashMap<>();
		
		for(ParsingEntity pe : fieldComputers.keySet()) {
			FieldComputer<?> oldFC = fieldComputers.get(pe);
			
			FieldComputer<?> fc = oldNewFC.containsKey(oldFC) ? oldNewFC.get(oldFC) : oldFC.clone();
			fc.setFields(nfields);
			nfieldComputers.put(pe, fc);
			oldNewFC.put(oldFC, fc);
		}
		
		return new CompiledRule(language, nfields, nfieldComputers);
	}

	public Map<String, Field<?>> getFields() { return fields; }
	
	public void reset() {
		for(Field<?> field : fields.values()) {
			field.reset();
		}
		for(FieldComputer<?> fc : fieldComputers.values()) {
			fc.reset();
		}
	}

}
