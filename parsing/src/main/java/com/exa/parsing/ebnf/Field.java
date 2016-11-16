package com.exa.parsing.ebnf;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.expressions.FDArray;
import com.exa.parsing.ebnf.expressions.FDObject;
import com.exa.parsing.ebnf.expressions.FDString;

public class Field<T> implements Cloneable {
	protected String name;
	protected T value;

	public Field(String name) {
		super();
		this.name = name;
		
		reset();
	}

	public T getValue() { return value; }

	@Override
	public Field<T> clone()  { return new Field<>(name); }
	
	public boolean manageModifNotif(Parsing<?> parsing, ParsingEvent peEvents) { return true; }

	public void setValue(T value) {	this.value = value;	}
	
	public FDString asStringField() { return null; }
	
	public FDArray asArrayField() { return null; }
	
	public FDObject asObjectField() { return null; }
	
	public void reset() { value = null; }

	public String getName() { return name; }

	public ParsedObject<?> valueAsParsedObject() { return null;	}
}
