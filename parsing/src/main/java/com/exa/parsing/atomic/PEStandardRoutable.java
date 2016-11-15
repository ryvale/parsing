package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.PERule;
import com.exa.parsing.PETWithPE;
import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ParsingRouter;
import com.exa.utils.ManagedException;

public class PEStandardRoutable extends PEAtomic {
	protected ParsingRouter router;

	public PEStandardRoutable(ParsingEntity peRoot, ParsingRouter router, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(peRoot, instancePEForNotify, nextPET);
		this.router = router;
	}
	
	public PEStandardRoutable(ParsingEntity peRoot) {
		this(peRoot, new ParsingRouter(PE_INSTANCE), null, PETransformer.petEOS());
	}
	
	public PEStandardRoutable(ParsingRouter router, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		this(NULL_PE, router, instancePEForNotify, nextPET);
	}

	public PEStandardRoutable() {
		this(new ParsingRouter(PE_INSTANCE), null, PETransformer.petEOS());
	}
	
	public PEStandardRoutable addRoute(PERule rule, PETransformer pet) {
		router.addRoute(rule, pet);
		return this;
	}
	
	public PEStandardRoutable addPrimaryRouteForNext(PERule rule) {
		router.addPrimaryRoute(rule, nextPET);
		return this;
	}

	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		return new RoutableInstance(this, router, peRoot).checkResult(parsing, sequence, pevs);
	}
	
}
