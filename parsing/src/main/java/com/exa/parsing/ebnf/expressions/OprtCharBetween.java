package com.exa.parsing.ebnf.expressions;

import com.exa.expression.ComputedItem;
import com.exa.expression.StackEvaluator;
import com.exa.expression.XPressionException;
import com.exa.parsing.PECharIn;
import com.exa.parsing.ParsingEntity;

public class OprtCharBetween extends OperatorBase<ParsingEntity> {

	public OprtCharBetween(String symbol, int priority) {
		super(symbol, priority, 2);
	}

	@Override
	public void resolve(StackEvaluator<Item<?>> eval, int order, int nbOperands) throws XPressionException {
		if(eval.stackLength() < nbOperands) throw new XPressionException("Invalid number of argument for '"+symbol+"'. 2 expected.");
		
		ComputedItem<Item<?>> ci = eval.popOperand();
		
		Item<?> item = ci.item();
		Operand<?> oprd = item.asOperand();
		if(oprd == null) {
			item.asOperator().resolve(eval, ci.order(), ci.asComputedOperator().nbOperand());
			oprd = eval.popOperand().item().asOperand();
		}
		
		Operand<String> opstr = oprd.asOPString();
		if(opstr == null) throw new XPressionException("Invalid argument for '"+symbol+"'.");
		
		String str = opstr.value();
		if(str.length() != 1) throw new XPressionException("Invalid argument '"+str+"' for '"+symbol+"'."); 
		char c2 = str.charAt(0);
		
		
		ci = eval.popOperand();
		
		item = ci.item();
		oprd = item.asOperand();
		if(oprd == null) {
			item.asOperator().resolve(eval, ci.order(), ci.asComputedOperator().nbOperand());
			oprd = eval.popOperand().item().asOperand();
		}
		
		opstr = oprd.asOPString();
		if(opstr == null) throw new XPressionException("Invalid argument for '"+symbol+"'.");
		
		str = opstr.value();
		if(str.length() != 1) throw new XPressionException("Invalid argument '"+str+"' for '"+symbol+"'.");
		char c1 = str.charAt(0);
		
		if(c2<c1) throw new XPressionException("Invalid argument '"+c1+"', '"+c2+"' for '"+symbol+"'.");
		
		eval.pushOperand(new ConstantParsingEntity(new PECharIn(c1, c2)));
	}

	@Override
	public boolean isPostUnary() { return false; }

	@Override
	public boolean isPreUnary() { return false; }

}
