package com.exa.parsing;

import java.util.LinkedList;
import java.util.List;

import com.exa.utils.ManagedException;


public class ParsingRouter {
	protected LinkedList<ConditionalAction> pActions;
	protected ParsingEntity defaultPE;

	public ParsingRouter(LinkedList<ConditionalAction> pActions, ParsingEntity defaultPE) {
		super();
		this.pActions = pActions;
		this.defaultPE = defaultPE;
	}
	
	public ParsingRouter(ParsingEntity defaultPE) { this(new LinkedList<ConditionalAction>(), defaultPE); }
	
	public ParsingRouter addRoute(ConditionalAction ca) {
		pActions.add(ca);
		return this;
	}
	
	public ParsingRouter addPrimaryRoute(ConditionalAction ca) {
		pActions.addFirst(ca);
		return this;
	}
	
	public ParsingRouter addRoute(PERule rule, PETransformer pet) {
		pActions.add(new ConditionalAction(rule, pet, ParsingEntity.DEFAULT_FAIL));
		return this;
	}
	
	public ParsingRouter addRoute(PERule rule, ParsingEntity pe) {
		return addRoute(rule, new PETIdentity(pe));
	}
	
	public ParsingRouter addPrimaryRoute(PERule rule, PETransformer pet) {
		pActions.addFirst(new ConditionalAction(rule, pet, ParsingEntity.DEFAULT_FAIL));
		return this;
	}
	
	public ParsingEntity route(ParsingEntity currentPE, Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
		ParsingEntity res = defaultPE;
		
		for(ConditionalAction task : pActions) {
			res = task.execute(currentPE, parsing, pevs);
			if(res == defaultPE) continue;
			
			break;
		}
		
		return res;
	}
	
}
