package com.exa.parsing.ebnf.expressions;

import java.util.HashMap;
import java.util.Map;

import com.exa.expression.OperatorMan;
import com.exa.expression.StackEvaluator;

public class OperatorManager extends OperatorMan<Item<?>> {
	protected Map<String, OperatorSymbol> operators;

	public OperatorManager(StackEvaluator<Item<?>> evaluator, Map<String, OperatorSymbol> operators) {
		super(evaluator);
		this.operators = operators;
		//													priority
		add(new OSUnique<>(new OprtMany(		"*", 		1, 			0)));
		add(new OSUnique<>(new OprtMany(		"+",		1, 			1)));
		add(new OSUnique<>(new OprtOptional(	"?",		1			 )));
		add(new OSUnique<>(new OprtNegate(		"!",		1			 )));
		
		add(new OSUnique<>(new OprtCharBetween(	"..",		2			 )));
		
		add(new OSUnique<>(new OprtOr(			"|",		3			 )));
	}
		
	public OperatorManager(StackEvaluator<Item<?>> evaluator) {
		this(evaluator, new HashMap<String, OperatorSymbol>());
	}

	@Override
	public Operator<?> parsed(String symbol) {
		OperatorSymbol os = operators.get(symbol);
		if(os == null) return null;
		
		OperatorBase<?> op = os.operatorOf(evaluator);
		if(op == null) return null;
		
		return op;
	}
	
	public OperatorSymbol add(OperatorSymbol os) {
		return operators.put(os.symbol(), os);
	}
	
}
