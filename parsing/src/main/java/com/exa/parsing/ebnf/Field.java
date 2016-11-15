package com.exa.parsing.ebnf;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEvent;

public class Field<T> implements Cloneable {
	protected String name;
	//protected boolean manageNotif;
	protected T value = null;

	public Field(String name/*, ParsingEntity parsingEntity, FieldFunction<T> computer*/) {
		super();
		//this.parsingEntity = parsingEntity;
		this.name = name;
		//this.computer = computer;
	}

	/*public ParsingEntity getParsingEntity() { return parsingEntity;	}

	public void setParsingEntity(ParsingEntity parsingEntity) {
		this.parsingEntity = parsingEntity;
	}*/

	//public T newValue(String v) { return value = computer.compute(value, v); }
	
	public T getValue() { return value; }

	public String getName() { return name; }
	
	//public ParsedObject<T> valueAsParsedObject() { return computer.asParsedObject(value); }

	@Override
	public Field<T> clone()  {
		return new Field<>(name);
	}
	
	
	public boolean manageModifNotif(Parsing<?> parsing, ParsingEvent peEvents) { return true; }

	public void setValue(T value) {	this.value = value;	}
	
	public Field<String> asStringField() { return null; }
	
	public FDStringArray asStringArrayField() { return null; }
}
