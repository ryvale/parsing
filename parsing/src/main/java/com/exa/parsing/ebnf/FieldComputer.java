package com.exa.parsing.ebnf;

import java.util.Map;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.expressions.FieldFunction;

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
		
	public void newValue(Parsing<?> parsing, String str) {
		Field<T> field = function.getFrom(fields.get(fieldName));
		field.setValue(function.compute(parsing, field.getValue(), str));
	}
	
	public String getName() { return fieldName; }
	
	public T getValue() { 
		return function.getFrom(fields.get(fieldName)).getValue(); 
	}
	
	public ParsedObject<T> valueAsParsedObject() { return function.asParsedObject(function.getFrom(fields.get(fieldName)).getValue()); }
	
	public boolean doesntManageModifNotif(ParsingEvent peEvents) { return function == null; }

	public Field<T> getField() { return function.getFrom(fields.get(fieldName)); }

	@Override
	public FieldComputer<T> clone() {
		return new FieldComputer<>(fields, fieldName, function);
	}

	public Map<String, Field<?>> getFields() {	return fields; }

	public void setFields(Map<String, Field<?>> fields) { this.fields = fields; }
	
}
