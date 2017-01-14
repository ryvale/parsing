package com.exa.parsing.ebnf;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.exa.parsing.ExpMan;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.expressions.FieldComputer;
import com.exa.utils.ManagedException;

public class OutputExpMan extends ExpMan<ParsedMap> {
	protected ParsedMap result;
	protected CompiledRule compiledRule;
	protected int computingRef = 0;
	
	public OutputExpMan(Parsing<ParsedMap> parsing, CompiledRule compiledRule, ParsedMap result) {
		super(parsing);
		this.result = result;
		this.compiledRule = compiledRule.clone();
	}
	
	public OutputExpMan(Parsing<ParsedMap> parsing, CompiledRule compiledRule) { this(parsing, compiledRule, new ParsedMap()); }
	
	@Override
	public ParsingEntity push(ParsingEntity currentPE, List<ParsingEvent> peEvents)	throws ManagedException {
		
		Iterator<ParsingEvent> it = peEvents.iterator();
		while(it.hasNext()) {
			ParsingEvent pev = it.next();
			
			ParsingEntity pe = pev.getParsingEntity();
			
			FieldComputer<?> computer = compiledRule.getFieldComputer(pe);
			
			if(computer == null) continue;
			
			if(computer.manageModifNotif(pev)) continue;
			
			computer.newValue(pev);
		}
		
		/*for(ParsingEvent pev : peEvents) {
			ParsingEntity pe = pev.getParsingEntity();
			FieldComputer<?> computer = compiledRule.getFieldComputer(pe);
			
			if(computer == null) continue;
			if(computer.manageModifNotif(pev)) continue;
			
			computer.newValue(parsing, pev.getWord());
		}*/
		return currentPE;
	}
	
	@Override
	public ParsedMap compute() throws ManagedException {
		//Map<ParsingEntity, FieldComputer<?>> fields = compiledRule.getFieldComputers();
		
		Map<String, Field<?>> fields = compiledRule.getFields();
		
		for(Field<?> field : fields.values()) {
			if(field.getValue() == null) continue;
			result.setValue(field.getName(), field.valueAsParsedObject());
		}
		
		return result;
	}
	
	@Override
	public ParsedMap lastResult() {	return result; }


	@Override
	public ParsedMap reset() {
		result.getValue().clear();
		
		compiledRule.reset();
		
		return result;
	}
	
}
