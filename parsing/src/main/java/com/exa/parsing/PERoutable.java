package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class PERoutable extends ParsingEntity {
	protected ParsingEntity peRoot;
	protected ParsingRouter router;
	
	
	public PERoutable(ParsingEntity peRoot, ParsingRouter router, PETWithPE nextPET) {
		super(nextPET);
		this.peRoot = peRoot;
		this.router = router;
	}
	
	public PERoutable(ParsingEntity peRoot, ParsingRouter router) {
		this(peRoot, router, PETransformer.petEOS());
	}
	
	public PERoutable(ParsingEntity peRoot) { this(peRoot, new ParsingRouter(DEFAULT_FAIL), PETransformer.petEOS()); }
	
	public PERoutable() { this(PE_NULL, new ParsingRouter(DEFAULT_FAIL));}


	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		ParsingEntity currentPE = peRoot.check(parsing, pevs);
		
		return router.route(currentPE, parsing, pevs);
	}
	
	public PERoutable addRoute(PERule rule, PETransformer pet) {
		router.addRoute(rule, pet);
		return this;
	}
	
	public PERoutable addPrimaryRouteForNext(PERule rule) {
		router.addPrimaryRoute(rule, nextPET);
		return this;
	}
}
