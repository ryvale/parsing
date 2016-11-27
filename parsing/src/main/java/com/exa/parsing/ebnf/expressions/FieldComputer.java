package com.exa.parsing.ebnf.expressions;

import java.util.Map;

import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedObject;

public class FieldComputer<T> implements Cloneable {
	protected Map<String, Field<?>> fields;
	protected FieldFunction<T> function;
	protected String fieldName;
	
	public FieldComputer(Map<String, Field<?>> fields, String fieldName, FieldFunction<T> function) {
		this.fields = fields;
		this.fieldName = fieldName;
		this.function = function;
	}
	
	public FieldComputer(String fieldName, FieldFunction<T> function) {
		this(null, fieldName, function);
	}
		
	public void newValue(ParsingEvent pev) {
		Field<T> field = function.getFrom(fields.get(fieldName));
		field.setValue(function.compute(pev.getParsing(), field.getValue(), pev.getWord()));
	}
	
	public String getFieldName() { return fieldName; }
	
	public T getValue() { 
		return function.getFrom(fields.get(fieldName)).getValue(); 
	}
	
	public ParsedObject<T> valueAsParsedObject() { return function.asParsedObject(function.getFrom(fields.get(fieldName)).getValue()); }
	
	public boolean manageModifNotif(ParsingEvent peEvents) { return function == null; }

	public Field<T> getField() { return function.getFrom(fields.get(fieldName)); }

	@Override
	public FieldComputer<T> clone() {
		return new FieldComputer<>(fields, fieldName, function);
	}

	public Map<String, Field<?>> getFields() {	return fields; }

	public void setFields(Map<String, Field<?>> fields) { this.fields = fields; }
	
	public void reset() {
		
	}
	
}
