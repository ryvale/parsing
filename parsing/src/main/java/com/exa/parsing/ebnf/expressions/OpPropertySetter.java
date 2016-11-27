package com.exa.parsing.ebnf.expressions;

import java.util.Map;

import com.exa.expression.ComputedItem;
import com.exa.expression.StackEvaluator;
import com.exa.expression.XPressionException;
import com.exa.parsing.PEAtomic;
import com.exa.parsing.PEWord;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.CompiledRule;
import com.exa.parsing.ebnf.Field;
import com.exa.parsing.ebnf.RuleParser;
import com.exa.parsing.ebnf.RuleScript;
import com.exa.parsing.ebnf.expressions.Evaluator.FieldMan;
import com.exa.utils.ManagedException;

public class OpPropertySetter<T> extends OperatorBase<T> {
	protected FieldMan fieldMan;
	protected FieldFunction<T> function;
	protected RuleParser ruleParser;

	public OpPropertySetter(RuleParser ruleParser, FieldMan fieldMan, String symbol, int priority, FieldFunction<T> function) {
		super(symbol, priority, 2);
		this.function = function;
		this.ruleParser = ruleParser;
		this.fieldMan = fieldMan;
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
		
		CompiledRule cr = null;
		Operand<ParsingEntity> oppe = oprd.asOPParsingEntity();
		if(oppe == null) {
			Operand<String> opId = oprd.asOPIdentifier();
			if(opId == null) {
				Operand<String> opstr = oprd.asOPString();
				if(opstr == null) throw new XPressionException("Invalid argument for '"+symbol+"'.");
				oppe = new ConstantParsingEntity(new PEWord(opstr.value()));
			}
			else {
				RuleScript rs = ruleParser.getRuleConfig().getRule(opId.value());
			
				try { cr = ruleParser.parse(rs.src()); } catch (ManagedException e) { throw new XPressionException(e); }
				ParsingEntity peRoot = cr.language().getPERoot();
				oppe = new ConstantParsingEntity(peRoot.getNextPE().isFinal() ? peRoot : new PEAtomic(cr.language().getPERoot()));
			}
		}
		
		ci = eval.popOperand();
		item = ci.item();
		oprd = item.asOperand();
		if(oprd == null) {
			item.asOperator().resolve(eval, ci.order(), ci.asComputedOperator().nbOperand());
			oprd = eval.popOperand().item().asOperand();
		}
		Operand<String> opstr = oprd.asOPIdentifier();
		if(opstr == null) throw new XPressionException("Invalid  argument for '"+symbol+"'.");
		
		ParsingEntity pe = oppe.value();
				
		addListenerForField(pe, opstr.value(), cr);
		
		eval.pushOperand(oppe);	
	}
	
	
	public void addListenerForField(ParsingEntity pe, String fieldName, CompiledRule cr) {
		if(cr == null) { addListenerForField(pe, fieldName); return; }
		
		if(cr.getFieldComputers().size()>0) {
			CompiledRule crf = cr.clone();
			
			Map<ParsingEntity, FieldComputer<?>> nfieldComputers = crf.getFieldComputers();
			
			for(ParsingEntity npe : nfieldComputers.keySet()) {
				FieldComputer<?> fc = nfieldComputers.get(npe);
				
				fieldMan.map(npe, new FCObject<>(crf, fc));
			}
			Field<?> field = fieldMan.getField(fieldName);
			if(field == null) fieldMan.add(new FDObject(fieldName, crf.getFields()));
			return;
		}
		
		addListenerForField(pe, fieldName);
	}
	
	public void addListenerForField(ParsingEntity pe, String name) {
		Field<T> field = function.getFrom(fieldMan.getField(name));
		if(field == null) fieldMan.add(field = function.createField(name));
		
		fieldMan.map(pe, new FieldComputer<>(fieldMan.getFields(), name, function));
	}

	@Override
	public boolean isPostUnary() { return false; }

	@Override
	public boolean isPreUnary() { return false;	}

	@Override
	public boolean canManage(com.exa.expression.Operand<Item<?>> oprd, int order) {
		Operand<?> soprd = oprd.asSpecificItem().asOperand();
		
		if(order > 1) {
			if(soprd.asOPString() != null) return true;
			if(soprd.asOPParsingEntity() != null) return true;
			if(soprd.asOPIdentifier() != null) return true;
		}
		
		if(order == 1) return soprd.asOPIdentifier() != null;
		
		return false;
	}

}
