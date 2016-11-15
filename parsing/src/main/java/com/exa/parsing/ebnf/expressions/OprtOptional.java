package com.exa.parsing.ebnf.expressions;

import com.exa.expression.XPressionException;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.atomic.PEOptional;

public class OprtOptional extends UnaryOp<ParsingEntity> {

	public OprtOptional(String symbol, int priority) {
		super(symbol, priority);
	}

	@Override
	protected Operand<ParsingEntity> getResult(Operand<?> oprd) throws XPressionException {
		ParsingEntity pe = null;
		Operand<ParsingEntity> oppe = oprd.asOPParsingEntity();
		if(oppe == null) {
			Operand<String> opstr = oprd.asOPString();
			if(opstr == null) new XPressionException("Error in expression near '"+symbol+"'");
			
			pe = new PEWord(opstr.value());
		}
		else pe = oppe.value();
		
		return new ConstantParsingEntity(new PEOptional(pe));
	}

	@Override
	public boolean isPostUnary() { return true; }

	@Override
	public boolean canManage(com.exa.expression.Operand<Item<?>> oprd, int order) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
