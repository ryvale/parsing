package com.exa.parsing.ebnf.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedArray;
import com.exa.parsing.ebnf.ParsedMap;
import com.exa.parsing.ebnf.ParsedObject;

public class FCObjectArray extends FieldComputer<List<ParsedObject<?>>> {
	protected List<ParsedObject<?>> value = new ArrayList<>();
	protected Field<List<ParsedObject<?>>> field = null;
	protected ParsedMap currentValue = null;
	protected ParsingEntity parsingEntity;
	
	protected Map<ParsingEntity, FieldComputer<?>> fieldMap = new HashMap<>();
	
	public FCObjectArray(ParsingEntity parsingEntity, Map<String, Field<?>> fields, String fieldName) {
		super(fields, fieldName, null);
		this.parsingEntity = parsingEntity;
	}

	@Override
	public void newValue(ParsingEvent pev) {
		ParsingEntity pe = pev.getParsingEntity();
		if(pe == parsingEntity) {
			currentValue = new ParsedMap();
			value.add(currentValue);
		}
		
		FieldComputer<?> fc = fieldMap.get(pe);
		if(fc == null) return;
		
		fc.newValue(pev);
		currentValue.setValue(fc.getFieldName(), fc.valueAsParsedObject());
	}

	@Override
	public ParsedObject<List<ParsedObject<?>>> valueAsParsedObject() {
		return new ParsedArray(value);
	}
	
	@Override
	public void setFields(Map<String, Field<?>> fields) {
		field = fields.get(fieldName).asArrayField();
		field.setValue(value);
		
		super.setFields(fields);
	}
	
	
	
	@Override
	public void reset() {
		value.clear();
		
		if(field == null) return;
		
		field.setValue(value);
	}
	
	public void addFieldMap(ParsingEntity pe, FieldComputer<?> fc) {
		fieldMap.put(pe, fc);
	}

	@Override
	public FCObjectArray clone() {
		FCObjectArray fcoa = new FCObjectArray(parsingEntity, fields, fieldName);
		
		for(ParsingEntity pe : fieldMap.keySet()) {
			fcoa.addFieldMap(pe, fieldMap.get(pe).clone());
		}
		
		return fcoa;
	}

	@Override
	public boolean manageModifNotif(ParsingEvent peEvents) {
		return false;
	}

	
}
