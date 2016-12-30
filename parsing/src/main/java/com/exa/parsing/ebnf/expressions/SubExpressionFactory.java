package com.exa.parsing.ebnf.expressions;

import java.util.HashMap;
import java.util.Map;

//import com.exa.expression.ExpressionOperand;
import com.exa.expression.StackEvaluator;
import com.exa.expression.SubExpOperandMan;

public class SubExpressionFactory extends com.exa.expression.SubExpressionFactory<Item<?>> {
	
	@Override
	public Map<String, SubExpOperandMan<Item<?>>> subExpManagers(StackEvaluator<Item<?>> evaluator) {
		Map<String, SubExpOperandMan<Item<?>>> subExpManagers = new HashMap<String, SubExpOperandMan<Item<?>>>();
		
		subExpManagers.put("(", new OpdManSubExp(evaluator, "(", ")"));
		
		subExpManagers.put("[", new OpdManSubExp(evaluator, "[", "]"));
		
		return subExpManagers;
	}
	
	

}
