package com.exa.parsing.ebnf.expressions;

import java.util.List;
import java.util.Map;

import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.CompiledRule;
import com.exa.parsing.ebnf.FDString;
import com.exa.parsing.ebnf.FDStringArray;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.FieldComputer;
import com.exa.parsing.ebnf.ObjectField;
import com.exa.parsing.ebnf.RuleParser;
import com.exa.parsing.ebnf.StringArrayFieldComputer;
import com.exa.parsing.ebnf.expressions.Evaluator.FieldMan;

public class OpStringArrayPropertySetter extends OpPropertySetter<List<String>> {
	protected FieldFunction<String> itemFunction;

	public OpStringArrayPropertySetter(RuleParser ruleParser, FieldMan fieldMan, String symbol, int priority, FieldFunction<String> function) {
		super(ruleParser, fieldMan, symbol, priority, null);
		this.itemFunction = function;
	}

	@Override
	public void addListenerForField(ParsingEntity pe, String fieldName, CompiledRule cr) {
		if(cr == null) { addListenerForField(pe, fieldName); return; }
		
		if(cr.getFieldComputers().size()>0) {
			Map<ParsingEntity, FieldComputer<?>> nfieldComputers = cr.clonedFieldComputers();
			ObjectField of = new ObjectField(fieldName, nfieldComputers);
			
			for(ParsingEntity npe : nfieldComputers.keySet()) {
				fieldMan.map(npe, new FieldComputer<>(fieldMan.getFields(), fieldName, null));
			}
			return;
		}
		
		addListenerForField(pe, fieldName);
	}

	protected void addListenerForField(ParsingEntity pe, String fieldName) {
		Field<?> f = fieldMan.getField(fieldName);
		Field<List<String>> field = f == null ? null : f.asStringArrayField();
		if(field == null) fieldMan.add(field = new FDStringArray(fieldName));
		
		fieldMan.map(pe, new StringArrayFieldComputer(fieldMan.getFields(), fieldName, itemFunction));
	}
	
	

}
