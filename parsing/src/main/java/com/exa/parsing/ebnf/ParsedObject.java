package com.exa.parsing.ebnf;

public class ParsedObject<T> {
	protected T value;
	
	public ParsedObject(T value) {
		super();
		this.value = value;
	}

	public ParsedString asParsedString() { return null; }
	public ParsedMap asParsedMap() { return null; }
	public ParsedArray asParsedArray() { return null; }
	
	public Object get(String field) { return null;}
	public Object get(int i) { return null;}

	public T getValue() { return value; }
	
	@Override
	public String toString() { return null; }
	
	
	public void setValue(T value) { this.value = value; }
	
	
}
