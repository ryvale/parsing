package com.exa.parsing.ebnf.expressions;

import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.CompiledRule;
import com.exa.parsing.ebnf.ParsedObject;

public class FCObject<T> extends FieldComputer<T> {
	protected FieldComputer<T> computer;
	private CompiledRule compiledRule;

	public FCObject(CompiledRule cr, FieldComputer<T> computer) {
		super(cr.getFields(), computer.getFieldName(), null);
		this.computer = computer;
		this.compiledRule = cr;
	}

	@Override
	public boolean manageModifNotif(ParsingEvent pev) {
		computer.newValue(pev);
		
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
	public FCObject<T> clone() {
		return new FCObject<>(compiledRule, computer);
	}
	
	
}
