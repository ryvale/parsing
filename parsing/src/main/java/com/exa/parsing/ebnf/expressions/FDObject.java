package com.exa.parsing.ebnf.expressions;


import java.util.HashMap;
import java.util.Map;

import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedMap;
import com.exa.parsing.ebnf.ParsedObject;

public class FDObject extends Field<Map<String, ParsedObject<?>>> {
	
	protected Map<String, Field<?>> properties;

	public FDObject(String name, Map<String, Field<?>> properties) {
		super(name);
		
		this.properties = properties;
	}

	@Override
	public Map<String, ParsedObject<?>> getValue() {
		for(Field<?> field : properties.values()) {
			value.put(field.getName(), field.valueAsParsedObject());
		}
		return value;
	}

	@Override
	public ParsedObject<?> valueAsParsedObject() {
		return new ParsedMap(getValue());
	}

	@Override
	public FDObject clone() {
		return new FDObject(name, properties);
	}

	public Map<String, Field<?>> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Field<?>> properties) {
		this.properties = properties;
	}

	@Override
	public void reset() {
		value = new HashMap<>();
	}
	
	

}
