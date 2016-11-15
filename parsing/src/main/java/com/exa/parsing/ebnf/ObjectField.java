package com.exa.parsing.ebnf;

import java.util.Map;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;

public class ObjectField extends Field<Object> {
	
	protected Map<ParsingEntity, FieldComputer<?>> properties;

	public ObjectField(String name, Map<ParsingEntity, FieldComputer<?>> properties) {
		super(name);
		
		this.properties = properties;
	}

	@Override
	public boolean manageModifNotif(Parsing<?> parsing, ParsingEvent pev) {
		FieldComputer<?> property = properties.get(pev.getParsingEntity());
		if(property == null) return true;
		
		property.newValue(parsing, pev.getWord());
		
		return true;
	}
	
	

}
