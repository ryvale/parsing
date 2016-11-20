package com.exa.parsing.ebnf.expressions;

import com.exa.expression.StackEvaluator;
import com.exa.expression.XPressionException;
import com.exa.parsing.PEOr;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;
//import com.exa.parsing.atomic.PEOr;

public class OprtOr extends OperatorBase<ParsingEntity> {

	public OprtOr(String symbol, int priority) {
		super(symbol, priority, 2);
	}

	@Override
	public void resolve(StackEvaluator<Item<?>> eval, int order, int nbOperands) throws XPressionException {
		if(eval.stackLength() < nbOperands) throw new XPressionException("Error in the expression near '"+ symbol +"'");
		
		PEOr peOr = new PEOr();
		
		for(int i=0; i<nbOperands; i++) {
			Operand<?> oprd = eval.popComputedOperand().asSpecificItem().asOperand();
			Operand<ParsingEntity> oppe = oprd.asSpecificItem().asOperand().asOPParsingEntity();
			if(oppe == null) {
				Operand<String> opstr = oprd.asOPString();
				if(opstr == null) throw new XPressionException("Error in the expression near '"+ symbol +"'");
				
				oppe = new ConstantParsingEntity(new PEWord(opstr.value()));
			}
			
			peOr.add(oppe.value());
		}
		
		eval.pushOperand(new ConstantParsingEntity(peOr));
	}

	@Override
	public boolean isPostUnary() { return false; }

	@Override
	public boolean isPreUnary() { return false; }
	

	@Override
	public boolean operandsAreCumulable() {	return true; }

	@Override
	public boolean canManage(com.exa.expression.Operand<Item<?>> oprd, int order) {
		return true;
	}
	
	

}
