package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class ConditionalAction {
	protected PERule rule;
	protected PETransformer pet;
	protected ParsingEntity peRuleFail;

	public ConditionalAction(PERule rule, PETransformer pet, ParsingEntity peRuleFail) {
		this.rule = rule;
		this.pet = pet;
		this.peRuleFail = peRuleFail;
		
	}

	public ParsingEntity execute(ParsingEntity currentPE, Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
		if(rule.isOK(currentPE, parsing)) return pet.get(currentPE, parsing, pevs);
		
		return peRuleFail;
	}

}
