package com.exa.parsing.ebnf;

import java.util.ArrayList;
import java.util.List;

public class FDStringArray extends Field<List<String>> {
	

	public FDStringArray(String name) {
		super(name);
		this.value = new ArrayList<>();
	}

	@Override
	public FDStringArray asStringArrayField() {	return this; }

	@Override
	public FDStringArray clone() {
		return new FDStringArray(name);
	}
	
	
}
