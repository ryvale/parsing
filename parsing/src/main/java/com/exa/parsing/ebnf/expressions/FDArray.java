package com.exa.parsing.ebnf.expressions;

import java.util.ArrayList;
import java.util.List;

import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedArray;
import com.exa.parsing.ebnf.ParsedObject;

public class FDArray extends Field<List<ParsedObject<?>>> {
	
	public FDArray(String name) {
		super(name);
	}

	@Override
	public FDArray asArrayField() {	return this; }

	@Override
	public FDArray clone() { return new FDArray(name); }

	@Override
	public void reset() {
		this.value = new ArrayList<>();
	}

	@Override
	public ParsedArray valueAsParsedObject() {
		return new ParsedArray(value);
	}
}
