package com.exa.parsing.ebnf.expressions;

import com.exa.parsing.Parsing;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedObject;
import com.exa.parsing.ebnf.ParsedString;

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
			return old + parsing.lexerBlankBefore() + nvalue;
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

/*	public void addListenerForField(FieldMan fieldMan, ParsingEntity pe, String name, CompiledRule cr) {
		if(cr == null) { addListenerForField(fieldMan, pe, name); return; }
		
		if(cr.getFieldComputers().size()>0) {
			CompiledRule crf = cr.clone();
			
			Map<ParsingEntity, FieldComputer<?>> nfieldComputers = crf.getFieldComputers();
			
			for(ParsingEntity npe : nfieldComputers.keySet()) {
				FieldComputer<?> fc = nfieldComputers.get(npe);
				
				fieldMan.map(npe, new ObjectFieldComputer<>(cr, fc));
			}
			return;
		}
		
		addListenerForField(fieldMan, pe, name);
	}
	
	public void addListenerForField(FieldMan fieldMan, ParsingEntity pe, String name) {
		Field<T> field = getFrom(fieldMan.getField(name));
		if(field == null) fieldMan.add(field = createField(name));
		
		fieldMan.map(pe, new FieldComputer<>(fieldMan.getFields(), name, this));
	}*/
}
