package com.exa.parsing.ebnf.expressions;

import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.CompiledRule;
import com.exa.parsing.ebnf.ParsedObject;

public class ObjectFieldComputer<T> extends FieldComputer<T> {
	protected FieldComputer<T> computer;
	private CompiledRule compiledRule;

	public ObjectFieldComputer(CompiledRule cr, FieldComputer<T> computer) {
		super(cr.getFields(), computer.getFieldName(), null);
		this.computer = computer;
		this.compiledRule = cr;
	}

	@Override
	public boolean manageModifNotif(ParsingEvent pev) {
		computer.newValue(pev.getParsing(), pev.getWord());
		
		return true;
	}

	@Override
	public void reset() {
		compiledRule.reset();
	}

	@Override
	public ParsedObject<T> valueAsParsedObject() {
		return computer.valueAsParsedObject();
	}

	@Override
	public ObjectFieldComputer<T> clone() {
		return new ObjectFieldComputer<>(compiledRule, computer);
	}
	
	
}
