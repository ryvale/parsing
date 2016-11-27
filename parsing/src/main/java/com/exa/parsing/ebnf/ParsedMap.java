package com.exa.parsing.ebnf;

import java.util.HashMap;
import java.util.Map;

public class ParsedMap extends ParsedObject<Map<String, ParsedObject<?>>> {
	
	public ParsedMap(Map<String, ParsedObject<?>> value) {
		super(value);
	}
	
	public ParsedMap() { this(new HashMap<String, ParsedObject<?>>()); }

	@Override
	public ParsedMap asParsedMap() { return this;	}
	
	public void setValue(String f, ParsedObject<?> v) {
		value.put(f, v);
	}
	
	public void setValue(String f, String v) {
		value.put(f, new ParsedString(v));
	}
	
	public boolean containsKey(String key) { return value.containsKey(key); }
	
	@Override
	public ParsedObject<?> get(String key) { return value.get(key); }
	
}
