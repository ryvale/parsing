package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class Instance extends ParsingEntity {
	protected PEAtomic peGenerator;
	protected ParsingEntity currentPE;

	public Instance(PEAtomic peGenerator, ParsingEntity currentPE) {
		super(PETransformer.petEOS());
		this.peGenerator = peGenerator;
		this.currentPE = currentPE;
	}
	
	public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException { 
		return currentPE = currentPE.check(parsing, pevs);
	}
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		ParsingEntity itPE = nextIteration(parsing, sequence, pevs);
		
		return itPE == PEAtomic.PE_INSTANCE ? this : itPE;
	}

	@Override
	public ParsingEntity peForNotify() {
		return peGenerator.getInstancePEForNotify();
	}

	@Override
	public boolean checkFinal() {
		return currentPE.checkFinal();
	}

	@Override
	public boolean isRoot() { return peGenerator.isRoot(); }
	
}
