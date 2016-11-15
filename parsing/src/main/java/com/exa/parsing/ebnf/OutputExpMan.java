package com.exa.parsing.ebnf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.exa.parsing.ExpMan;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class OutputExpMan extends ExpMan<ParsedMap> {
	protected ParsedMap result;
	protected CompiledRule compiledRule;
	protected int computingRef = 0;
	
	public OutputExpMan(Parsing<ParsedMap> parsing, CompiledRule compiledRule, ParsedMap result) {
		super(parsing);
		this.result = result;
		this.compiledRule = compiledRule;
	}
	
	public OutputExpMan(Parsing<ParsedMap> parsing, CompiledRule compiledRule) { this(parsing, compiledRule, new ParsedMap()); }
	
	@Override
	public ParsingEntity push(String exp, ParsingEntity currentPE, List<ParsingEvent> peEvents)	throws ManagedException {
		ParsingEntity res = super.push(exp, currentPE, peEvents);
		for(ParsingEvent pev : peEvents) {
			ParsingEntity pe = pev.getParsingEntity();
			FieldComputer<?> field = compiledRule.getFieldComputer(pe);
			
			if(field == null) continue;
			if(field.doesntManageModifNotif(pev)) continue;
			
			field.newValue(parsing, pev.getWord());
		}
		return res;
	}
	
	@Override
	public ParsedMap compute() throws ManagedException {
		Map<ParsingEntity, FieldComputer<?>> fields = compiledRule.getFieldComputers();
		
		for(FieldComputer<?> fieldComputer : fields.values()) {
			if(fieldComputer.getValue() == null) continue;
			result.setValue(fieldComputer.getName(), fieldComputer.valueAsParsedObject());
		}
		
		return result;
	}
	
	@Override
	public ParsedMap lastResult() {	return result; }


	@Override
	public void reset() {
		result.getValue().clear();
		Map<ParsingEntity, FieldComputer<?>> fieldComputers = compiledRule.getFieldComputers();
		Map<ParsingEntity, FieldComputer<?>> nfieldComputers = new HashMap<>();
		
		Iterator<FieldComputer<?>> it = fieldComputers.values().iterator();
		
		if(!it.hasNext()) return;
		
		FieldComputer<?> fc = it.next();
		
		Map<String, Field<?>> fields = fc.getFields();
		Map<String, Field<?>> nfields = new HashMap<>();
		for(String fname : fields.keySet()) {
			nfields.put(fname, fields.get(fname).clone());
		}
		
		for(ParsingEntity pe : fieldComputers.keySet()) {
			fc = fieldComputers.get(pe).clone();
			fc.setFields(nfields);
			nfieldComputers.put(pe, fc);
		}
		
		compiledRule = new CompiledRule(compiledRule.language(), nfieldComputers);
	}
	
}
