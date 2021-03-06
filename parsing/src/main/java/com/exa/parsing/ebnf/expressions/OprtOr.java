package com.exa.parsing.ebnf.expressions;

import com.exa.expression.StackEvaluator;
import com.exa.expression.XPressionException;
import com.exa.parsing.PEOr;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;

public class OprtOr extends OperatorBase<ParsingEntity> {

	public OprtOr(String symbol, int priority) {
		super(symbol, priority, 2);
	}

	@Override
	public void resolve(StackEvaluator<Item<?>> eval, int order, int nbOperands) throws XPressionException {
		if(eval.stackLength() < nbOperands) throw new XPressionException("Error in the expression near '"+ symbol +"'");
		
		PEOr peOr = null;
		
		PEWord peWord = new PEWord();
		
		for(int i=0; i<nbOperands; i++) {
			Operand<?> oprd = eval.popComputedOperand().asSpecificItem().asOperand();
			Operand<ParsingEntity> oppe = oprd.asSpecificItem().asOperand().asOPParsingEntity();
			if(oppe == null) {
				Operand<String> opstr = oprd.asOPString();
				if(opstr == null) {
					Operand<String> opId = oprd.asOPIdentifier();
					if(opId == null) throw new XPressionException("Error in the expression near '"+ symbol +"'");
					
					com.exa.expression.Operand<Item<?>> baseOprd = eval.operandReinterpreted(oprd);
					if(baseOprd == null) throw new XPressionException("Error in the expression near '"+ symbol +"'");
					
					oprd = baseOprd.asSpecificItem().asOperand();
					if((oppe = oprd.asOPParsingEntity()) == null) throw new XPressionException("Error in the expression near '"+ symbol +"'");
				}
				else {
					if(peWord == null) peOr.add(new PEWord(opstr.value()));
					else peWord.add(opstr.value());
					continue;
				}
			}
			if(peOr == null) {
				peOr = new PEOr();
				if(peWord.checkStringCount()>0)	peOr.add(peWord);
				peWord = null;
			}
			peOr.add(oppe.value());
		}
		
		eval.pushOperand(new ConstantParsingEntity(peWord == null ? peOr : peWord));
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
