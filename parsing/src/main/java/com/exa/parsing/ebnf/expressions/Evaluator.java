package com.exa.parsing.ebnf.expressions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.exa.expression.ComputedItem;
import com.exa.expression.StackEvaluator;
import com.exa.expression.XPressionException;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.CompiledRule;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.ParsedObject;
import com.exa.parsing.ebnf.RuleParser;
import com.exa.parsing.ebnf.RuleScript;
import com.exa.utils.ManagedException;

public class Evaluator extends StackEvaluator<Item<?>> {
	public class FieldMan {
		Map<ParsingEntity, FieldComputer<?>> fieldsParsingMap = new HashMap<>();
		Map<String, Field<?>> fields = new HashMap<String, Field<?>>();
		
		public void clear() { fields.clear(); fieldsParsingMap.clear();}
		
		public Field<?> getField(String name) { return fields.get(name); }
		
		public void map(ParsingEntity pe, FieldComputer<?> computer) {
			fieldsParsingMap.put(pe, computer);
		}
		
		public void add(Field<?> field) { fields.put(field.getName(), field); }

		public Map<String, Field<?>> getFields() { return fields; }
	}
	
	protected RuleParser ruleParser;
	protected FieldMan fieldManager = new FieldMan();
	//protected Map<ParsingEntity, Field<?>> fieldsParsingMap = new HashMap<ParsingEntity, Field<?>>();
	//protected Map<String, Field<?>> fields = new HashMap<String, Field<?>>();
	
	protected OpPropertySetter<String> opAssign;
	protected OpPropertySetter<String> opConcat;
	protected OpPropertySetter<List<ParsedObject<?>>> opArray;
	
	public Evaluator(RuleParser ruleParser) {
		super(new SubExpressionFactory());
		this.ruleParser = ruleParser;
		
		managers.add(new OpdManString(this));
		managers.add(new OpdManWord(this));
		managers.add(new OpdManIdentier(this));
		
		OperatorManager oprtMan = new OperatorManager(this);
		
		opAssign = new OpPropertySetter<>(ruleParser, fieldManager, "=", 2, FieldFunction.assigner);
		opConcat = new OpPropertySetter<>(ruleParser, fieldManager, "$=", 2, FieldFunction.concatener);
		opArray = new OpArrayPropertySetter(ruleParser, fieldManager, "[]=", 2);
		
		oprtMan.add(new OSUnique<>(opAssign));
		oprtMan.add(new OSUnique<>(opConcat));
		oprtMan.add(new OSUnique<>(opArray));
		
		managers.add(oprtMan);
	}
	
	public RuleParser getRuleParser() {	return ruleParser; }

	public Map<ParsingEntity, FieldComputer<?>> getFieldsParsingMap() { return fieldManager.fieldsParsingMap; }
	
	//public 
	
	public void reset() { 
		stack.clear();
		opStack.clear();
		fieldManager.clear();
		
		//opAssign.setFields(fieldManager);
		//opConcat.setFields(fieldManager);
	}

	@Override
	public Operand<?> compute() throws XPressionException {
		super.compute();
		Stack<ParsingEntity> resStack = new Stack<>();
		
		while(stack.size()>0) {
			ComputedItem<Item<?>> ci = stack.pop();
			Item<?> item = ci.item();
			Operator<?> oprt = item.asOperator();
			Operand<ParsingEntity> oppe;
			
			Operand<?> oprd = null;
			if(oprt == null) {
				oprd = item.asOperand();
				oppe = oprd.asOPParsingEntity();
				if(oppe == null) {
					
					Operand<String> opstr = oprd.asOPString();
					if(opstr == null) {
						Operand<String> opId = oprd.asOPIdentifier();
						if(opId == null) throw new XPressionException("Error evaluating expression");
						
						CompiledRule cr = null;
						RuleScript rs = ruleParser.getRuleConfig().getRule(opId.value());
						try { cr = ruleParser.parse(rs.src()); } catch (ManagedException e) { throw new XPressionException(e); }
						oppe = new ConstantParsingEntity(cr.language().getPERoot());
					}
					else oppe = new ConstantParsingEntity(new PEWord(opstr.value()));
				}
				
				resStack.push(oppe.value());
				continue;
			}
			
			oprt.resolve(this, ci.order(), ci.asComputedOperator().nbOperand());
			oprd = stack.pop().item().asOperand();
			if(oprd == null) throw new XPressionException("Error evaluating expression");
			oppe = oprd.asOPParsingEntity();
			if(oppe == null) throw new XPressionException("Error evaluating expression");
			
			resStack.push(oppe.value());
		}
		
		ParsingEntity peRoot = resStack.pop();
		
		ParsingEntity currentPE = peRoot;
		
		while(resStack.size()>0) {
			ParsingEntity pe = resStack.pop();
			currentPE.setNextPE(pe);
			currentPE = pe;
		}
		
		com.exa.parsing.ebnf.expressions.Operand<ParsingEntity> res = new ConstantParsingEntity(peRoot);
		
		pushOperand(res);
		
		return res;
	}

	public Map<String, Field<?>> getFields() { return fieldManager.getFields(); }

	@Override
	public Operand<?> operandReinterpreted(com.exa.expression.Operand<Item<?>> oprd) throws XPressionException {
		Operand<String> opId = oprd.asSpecificItem().asOperand().asOPIdentifier();
		if(opId == null) return oprd.asSpecificItem().asOperand();
		
		CompiledRule cr = null;
		RuleScript rs = ruleParser.getRuleConfig().getRule(opId.value());
		
		try { cr = ruleParser.parse(rs.src()); } catch (ManagedException e) { throw new XPressionException(e); }
		return new ConstantParsingEntity(cr.language().getPERoot());
		
	}
	
	
	
}
