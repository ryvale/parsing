package com.exa.parsing.ebnf.expressions;

import java.util.List;

import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedObject;
import com.exa.parsing.ebnf.RuleParser;
import com.exa.parsing.ebnf.expressions.Evaluator.FieldMan;

public class OpArrayPropertySetter extends OpPropertySetter<List<ParsedObject<?>>> {

	public OpArrayPropertySetter(RuleParser ruleParser, FieldMan fieldMan, String symbol, int priority) {
		super(ruleParser, fieldMan, symbol, priority, null);
	}

	/*@Override
	public void addListenerForField(ParsingEntity pe, String fieldName, CompiledRule cr) {
		if(cr == null) { addListenerForField(pe, fieldName); return; }
		
		if(cr.getFieldComputers().size()>0) {
			Map<ParsingEntity, FieldComputer<?>> nfieldComputers = cr.clonedFieldComputers();
			//ObjectField of = new ObjectField(fieldName, nfieldComputers);
			
			for(ParsingEntity npe : nfieldComputers.keySet()) {
				fieldMan.map(npe, new FieldComputer<>(fieldMan.getFields(), fieldName, null));
			}
			return;
		}
		
		addListenerForField(pe, fieldName);
	}*/

	@Override
	public void addListenerForField(ParsingEntity pe, String fieldName) {
		Field<?> f = fieldMan.getField(fieldName);
		Field<List<ParsedObject<?>>> field = f == null ? null : f.asArrayField();
		if(field == null) fieldMan.add(field = new FDArray(fieldName));
		
		fieldMan.map(pe, new StringArrayFieldComputer(fieldMan.getFields(), fieldName, FieldFunction.concatener));
	}
	
}
