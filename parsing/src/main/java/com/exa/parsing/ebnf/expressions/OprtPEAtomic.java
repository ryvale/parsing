package com.exa.parsing.ebnf.expressions;

import com.exa.expression.ComputedItem;
import com.exa.expression.StackEvaluator;
import com.exa.expression.XPressionException;
import com.exa.parsing.PEAtomic;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;

public class OprtPEAtomic extends OperatorBase<ParsingEntity> {

	public OprtPEAtomic() {
		super(null, 0, 0);
	}

	@Override
	public void resolve(StackEvaluator<Item<?>> eval, int order, int nbOperands) throws XPressionException {
		Operand<?> oprd = eval.popComputedOperand().asSpecificItem().asOperand();
		Operand<Integer> oprInt = oprd.asOPInteger();
		Integer nb = oprInt.value();
		
		ParsingEntity peRoot = null, currentPE = null;
		
		while(eval.stackLength() > nb) {
			ComputedItem<Item<?>> ci = eval.popOperand();
			Item<?> item = ci.item();
			oprd = item.asOperand();
			if(oprd == null) {
				item.asOperator().resolve(eval, ci.order(), ci.asComputedOperator().nbOperand());
				oprd = eval.popOperand().item().asOperand();
			}
			
			ParsingEntity pe = null;
			Operand<ParsingEntity> oppe = oprd.asOPParsingEntity();
			if(oppe == null) {
				Operand<String> opstr = oprd.asOPString();
				if(opstr == null) {
					oprd = eval.operandReinterpreted(oprd).asSpecificItem().asOperand();
					oppe = oprd.asOPParsingEntity();
					if(oppe == null) throw new XPressionException("Invalid expression");
					pe = oppe.value();
					
				}
				else pe = new PEWord(opstr.value());
			}
			else pe = oppe.value();
			
			if(currentPE == null) {
				peRoot = currentPE = pe;
			}
			else currentPE = currentPE.setNextPE(pe);
		}
		
		eval.pushOperand(new ConstantParsingEntity(new PEAtomic(peRoot)));
	}

	@Override
	public boolean isPostUnary() { return false; }

	@Override
	public boolean isPreUnary() { return false; }

}
