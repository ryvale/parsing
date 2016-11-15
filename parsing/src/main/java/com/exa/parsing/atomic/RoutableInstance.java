package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.parsing.ParsingRouter;
import com.exa.utils.ManagedException;

public class RoutableInstance extends Instance {
	protected ParsingRouter router;

	public RoutableInstance(PEAtomic peGenerator, ParsingRouter router, ParsingEntity currentPE) {
		super(peGenerator, currentPE);
		this.router = router;
	}

	@Override
	public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		currentPE = currentPE.check(parsing, pevs);
		
		return router.route(currentPE, parsing, pevs);
	}
	
}
