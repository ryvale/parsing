package com.exa.parsing.ebnf.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedArray;
import com.exa.parsing.ebnf.ParsedObject;
import com.exa.parsing.ebnf.ParsedString;

public class FCStringArray extends FieldComputer<List<ParsedObject<?>>> {
	protected List<ParsedObject<?>> value = new ArrayList<>();
	protected Field<List<ParsedObject<?>>> field = null;
	protected int currentIdx = 0;
	protected FieldFunction<String> itemFunction;
	protected boolean addAfterUpdate = false;
	private StringBuilder sbValue =  new StringBuilder();

	public FCStringArray(Map<String, Field<?>> fields, String fieldName, FieldFunction<String> function) {
		super(fields, fieldName, null);
		
		this.itemFunction = function;
	}

	@Override
	public void newValue(ParsingEvent pev) {
		
		value.add(new ParsedString(itemFunction.compute(pev.getParsing(), null, pev.getTrimWord()))); 
		
	}

	@Override
	public boolean manageModifNotif(ParsingEvent pev) {	return false; }

	@Override
	public void setFields(Map<String, Field<?>> fields) {
		field = fields.get(fieldName).asArrayField();
		field.setValue(value);
		
		super.setFields(fields);
	}

	@Override
	public ParsedObject<List<ParsedObject<?>>> valueAsParsedObject() {
		return new ParsedArray(value);
	}

	@Override
	public List<ParsedObject<?>> getValue() { return value; }

	@Override
	public FCStringArray clone() {
		return new FCStringArray(fields, fieldName, itemFunction);
	}

	@Override
	public void reset() {
		value.clear();
		//value.add(new ParsedString(null));
		
		sbValue.setLength(0);
		addAfterUpdate = false;
		currentIdx = 0;
		if(field == null) return;
		
		field.setValue(value);
	}
	
	
	
	
}
