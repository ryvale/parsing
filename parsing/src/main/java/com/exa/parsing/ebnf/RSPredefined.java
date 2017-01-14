package com.exa.parsing.ebnf;

import java.util.HashMap;

import com.exa.parsing.PEAtomic;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.expressions.FieldComputer;
import com.exa.utils.ManagedException;

public class RSPredefined extends RuleScript {
	protected ParsingEntity parsingEntity;

	public RSPredefined(String name, ParsingEntity parsingEntity) {
		super(name, null);
		this.parsingEntity = parsingEntity;
	}

	@Override
	public CompiledRule compileWith(RuleParser rp) throws ManagedException {
		RulesConfig rc = rp.getRulesConfig();
		
		return new CompiledRule(new PEAtomic(parsingEntity), rc.charsToIgnore(), rc.separators(), new HashMap<String, Field<?>>(), new HashMap<ParsingEntity, FieldComputer<?>>());
	}
	
	

}
