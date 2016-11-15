package com.exa.parsing.ebnf.expressions;

import java.util.Map;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.CompiledRule;
import com.exa.parsing.ebnf.FDString;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.FieldComputer;
import com.exa.parsing.ebnf.ObjectField;
import com.exa.parsing.ebnf.ParsedObject;
import com.exa.parsing.ebnf.ParsedString;
import com.exa.parsing.ebnf.expressions.Evaluator.FieldMan;

public abstract class FieldFunction<T> {
	
	public static final FieldFunction<String> assigner = new FieldFunction<String>() {

		@Override
		public String compute(Parsing<?> parsing, String old, String nvalue) {
			return nvalue;
		}

		@Override
		public ParsedObject<String> asParsedObject(String v) {
			return new ParsedString(v);
		}

		@Override
		public Field<String> createField(String name) {
			return new FDString(name);
		}

		@Override
		public Field<String> getFrom(Field<?> f) {
			if(f == null) return null;
			return f.asStringField();
		}
		
	};
	
	public static final FieldFunction<String> concatener = new FieldFunction<String>() {
		
		@Override
		public String compute(Parsing<?> parsing, String old, String nvalue) {
			if(old == null)	return nvalue;
			return old + parsing.readBlank() + nvalue;
		}
		
		@Override
		public ParsedObject<String> asParsedObject(String v) {
			return new ParsedString(v);
		}

		@Override
		public Field<String> createField(String name) {
			return new FDString(name);
		}

		@Override
		public Field<String> getFrom(Field<?> f) {
			if(f == null) return null;
			return f.asStringField();
		}
		
	};
	
	public static final FieldFunction<Object> object = new FieldFunction<Object>() {

		@Override
		public Object compute(Parsing<?> parsing, Object old, String nvalue) {
			
			return null;
		}

		@Override
		public ParsedObject<Object> asParsedObject(Object v) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Field<Object> createField(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Field<Object> getFrom(Field<?> f) {
			if(f == null) return null;
			return null;
		}
	};
	
	
	public abstract T compute(Parsing<?> parsing, T old, String nvalue);
	
	public abstract ParsedObject<T> asParsedObject(T v);
	
	public abstract Field<T> createField(String name);
	
	public abstract Field<T> getFrom(Field<?> f);

	public void addListenerForField(FieldMan fieldMan, ParsingEntity pe, String name, CompiledRule cr) {
		if(cr == null) { addListenerForField(fieldMan, pe, name); return; }
		
		if(cr.getFieldComputers().size()>0) {
			Map<ParsingEntity, FieldComputer<?>> nfieldComputers = cr.clonedFieldComputers();
			//ObjectField of = new ObjectField(name, nfieldComputers);
			
			for(ParsingEntity npe : nfieldComputers.keySet()) {
				fieldMan.map(npe, new FieldComputer<>(fieldMan.getFields(), name, null));
			}
			return;
		}
		
		addListenerForField(fieldMan, pe, name);
	}
	
	public void addListenerForField(FieldMan fieldMan, ParsingEntity pe, String name) {
		Field<T> field = getFrom(fieldMan.getField(name));
		if(field == null) fieldMan.add(field = createField(name));
		
		fieldMan.map(pe, new FieldComputer<>(fieldMan.getFields(), name, this));
	}
}
