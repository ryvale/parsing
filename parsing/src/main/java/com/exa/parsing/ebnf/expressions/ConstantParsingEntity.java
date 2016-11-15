package com.exa.parsing.ebnf.expressions;

import com.exa.parsing.ParsingEntity;

public class ConstantParsingEntity extends Constant<ParsingEntity> {

	public ConstantParsingEntity(ParsingEntity value) {
		super(value);
	}

	@Override
	public Operand<ParsingEntity> asOPParsingEntity() {
		return this;
	}
	
}
