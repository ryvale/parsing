package com.exa.parsing.ebnf.expressions;

import java.util.Map;

import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedObject;

public class StandardObjecFieldComputer extends FieldComputer<ParsedObject<?>> {

	public StandardObjecFieldComputer(Map<String, Field<?>> fields, String fieldName, FieldFunction<ParsedObject<?>> function) {
		super(fields, fieldName, function);
	}

}
