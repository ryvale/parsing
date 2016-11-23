package com.exa.parsing.ebnf.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedArray;
import com.exa.parsing.ebnf.ParsedObject;
import com.exa.parsing.ebnf.ParsedString;

public class StringArrayFieldComputer extends FieldComputer<List<ParsedObject<?>>> {
	protected List<ParsedObject<?>> value = new ArrayList<>();
	protected Field<List<ParsedObject<?>>> field = null;
	protected int currentIdx = 0;
	protected FieldFunction<String> itemFunction;
	protected boolean addAfterUpdate = false;
	private StringBuilder sbValue =  new StringBuilder();

	public StringArrayFieldComputer(Map<String, Field<?>> fields, String fieldName, FieldFunction<String> function) {
		super(fields, fieldName, null);
		
		this.itemFunction = function;
	}

	@Override
	public void newValue(Parsing<?> parsing, String str) {
		//ParsedString ps = value.get(currentIdx++).asParsedString();
		//sbValue.append(itemFunction.compute(parsing, ps.asParsedString().getValue(), str));
		//ps.setValue(sbValue.toString());
		value.add(new ParsedString(itemFunction.compute(parsing, null, str))); 
		/*if(addAfterUpdate) {
			value.add(new ParsedString(null)); 
			addAfterUpdate = false;
			sbValue.setLength(0);
		}*/
	}

	@Override
	public boolean manageModifNotif(ParsingEvent pev) {
		if(pev.getResult() == ParsingEntity.PE_REPEAT_END) {
			addAfterUpdate = true;
			//value.add(new ParsedString(null)); ++currentIdx;
		}
		
		return false;
	}

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
	public StringArrayFieldComputer clone() {
		return new StringArrayFieldComputer(fields, fieldName, itemFunction);
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
