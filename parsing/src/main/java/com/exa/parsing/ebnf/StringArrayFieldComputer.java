package com.exa.parsing.ebnf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ebnf.expressions.FieldFunction;

public class StringArrayFieldComputer extends FieldComputer<List<ParsedObject<?>>> {
	protected List<String> value = new ArrayList<>();
	protected int currentIdx = 0;
	protected FieldFunction<String> itemFunction;
	protected boolean addAfterUpdate = false;

	public StringArrayFieldComputer(Map<String, Field<?>> fields, String fieldName, FieldFunction<String> function) {
		super(fields, fieldName, null);
		value.add("");
		this.itemFunction = function;
	}

	@Override
	public void newValue(Parsing<?> parsing, String str) {
		String old = value.get(currentIdx);
		String nvalue = itemFunction.compute(parsing, old, str);
		
		value.set(currentIdx, nvalue);
		if(addAfterUpdate) {
			value.add(""); ++currentIdx;
			addAfterUpdate = false;
		}
	}

	@Override
	public boolean doesntManageModifNotif(ParsingEvent pev) {
		if(pev.getResult() == ParsingEntity.PE_REPEAT_END) {
			addAfterUpdate = true;
			//value.add(""); ++currentIdx;
		}
		
		return false;
	}

	@Override
	public void setFields(Map<String, Field<?>> fields) {
		Field<List<String>> field = fields.get(fieldName).asStringArrayField();
		field.setValue(value);
		
		super.setFields(fields);
	}

	@Override
	public ParsedObject<List<ParsedObject<?>>> valueAsParsedObject() {
		List<ParsedObject<?>> res = new ArrayList<>();
		
		for(String v : value) {
			res.add(new ParsedString(v));
		}
		
		return new ParsedArray(res);
	}

	@Override
	public List<ParsedObject<?>> getValue() {
		List<ParsedObject<?>> res = new ArrayList<>();
		
		for(String v : value) {
			res.add(new ParsedString(v));
		}
		
		return res;
	}

	@Override
	public StringArrayFieldComputer clone() {
		return new StringArrayFieldComputer(fields, fieldName, itemFunction);
	}
	
	
	
	
}
