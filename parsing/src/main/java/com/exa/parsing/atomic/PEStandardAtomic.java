package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.PETWithPE;
import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class PEStandardAtomic extends PEAtomic {
	
	class Instance extends com.exa.parsing.atomic.Instance {

		public Instance() {
			super(PEStandardAtomic.this, PEStandardAtomic.this.peRoot);
		}

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			super.nextIteration(parsing, sequence, pevs);
			
			if(currentPE.failed()) return currentPE;
			
			if(currentPE == PE_NEXT_CHECK) {
				ParsingEntity nextPE = PEStandardAtomic.this.nextPET.get(currentPE, parsing, pevs);
				if(nextPE.isFinal()) return PE_NEXT_CHECK;
				return nextPE.check(parsing, pevs);
			}
			
			if(currentPE.isFinal()) return PEStandardAtomic.this.nextPET.get(currentPE, parsing, pevs);
			
			return PE_INSTANCE;
		}
		
	}

	public PEStandardAtomic(ParsingEntity peRoot, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(peRoot, instancePEForNotify, nextPET);
	}
	
	public PEStandardAtomic(ParsingEntity peRoot) {
		this(peRoot, null, PETransformer.petEOS());
	}

	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		return new Instance().checkResult(parsing, sequence, pevs);
	}

}
