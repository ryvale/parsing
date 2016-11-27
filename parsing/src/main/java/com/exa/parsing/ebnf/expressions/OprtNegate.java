package com.exa.parsing.ebnf.expressions;

import com.exa.expression.XPressionException;
import com.exa.parsing.PENotWord;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;

public class OprtNegate extends UnaryOp<ParsingEntity> {

	public OprtNegate(String symbol, int priority) {
		super(symbol, priority);
	}

	@Override
	protected Operand<ParsingEntity> getResult(Operand<?> oprd) throws XPressionException {
		Operand<String> opstr = oprd.asOPString();
		if(opstr == null) {
			Operand<ParsingEntity> oppe = oprd.asOPParsingEntity();
			if(oppe == null) throw new XPressionException("Error in expression near '"+symbol+"'");
			
			PEWord peWord = oppe.value().asPEWord();
			if(peWord == null) throw new XPressionException("Error in expression near '"+symbol+"'");
			
			return new ConstantParsingEntity(new PENotWord(peWord.getRequiredStrings()));
		}
		
		return new ConstantParsingEntity(new PENotWord(opstr.value()));
	}

	@Override
	public boolean isPreUnary() { return true; }

	@Override
	public boolean canManage(com.exa.expression.Operand<Item<?>> oprd, int order) {
		Operand<?> soprd = oprd.asSpecificItem().asOperand();
		
		if(soprd.asOPString() != null) return true;
		return false;
	}
	
	


}
