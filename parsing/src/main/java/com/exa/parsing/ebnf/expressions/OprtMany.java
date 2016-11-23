package com.exa.parsing.ebnf.expressions;

import com.exa.expression.XPressionException;
import com.exa.parsing.PERepeat;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;

public class OprtMany extends UnaryOp<ParsingEntity> {
	protected int min;

	public OprtMany(String symbol, int priority, int min) {
		super(symbol, priority);
		this.min = min;
	}
	
	@Override
	protected Operand<ParsingEntity> getResult(Operand<?> oprd) throws XPressionException {
		ParsingEntity pe = null;
		Operand<ParsingEntity> oppe = oprd.asOPParsingEntity();
		
		if(oppe == null) {
			Operand<String> opstr = oprd.asOPString();
			if(opstr == null) {
				
				throw new XPressionException("Error in expression near '"+symbol+"'");
			}
			
			pe = new PEWord(opstr.value());
		}
		else pe = oppe.value();
		
		return new ConstantParsingEntity(new PERepeat(pe, min));
	}

	@Override
	public boolean isPostUnary() { return true;	}

	@Override
	public boolean canManage(com.exa.expression.Operand<Item<?>> oprd, int order) {
		//if(order == 1) {
		
		//}
		return true;
	}
	
	
}