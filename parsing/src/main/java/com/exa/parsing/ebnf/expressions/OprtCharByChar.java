package com.exa.parsing.ebnf.expressions;

import com.exa.parsing.PECharByChar;
import com.exa.parsing.ParsingEntity;

public class OprtCharByChar extends OprtPEAtomic {

	@Override
	protected ParsingEntity finalParsingEntity(ParsingEntity peResult, int nbPE) {
		return new PECharByChar(peResult);
	}

	

}
