package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.PETIdentity;
import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class PEOptional extends PEAtomic {
	
	class Instance extends com.exa.parsing.atomic.Instance {
		protected ParsingEntity nextCurrentPE;

		public Instance() {
			super(PEOptional.this, PEOptional.this.peRoot);
			nextCurrentPE = PEOptional.this.getNextPE();
		}

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			super.nextIteration(parsing, sequence, pevs);
			if(currentPE.failed()) {
				if(nextCurrentPE.isFinal()) return PE_NEXT_CHECK;
				parsing.registerResult(sequence, currentPE);
				return nextCurrentPE.check(parsing, pevs);
			}
			
			nextCurrentPE = nextCurrentPE.check(parsing, pevs);
			
			if(nextCurrentPE.failed()) return currentPE;
			
			return PE_INSTANCE;
		}
		
	}

	public PEOptional(ParsingEntity peRoot, ParsingEntity instancePEForNotify, PETIdentity nextPET) {
		super(peRoot, instancePEForNotify, nextPET);
	}
	
	public PEOptional(ParsingEntity peRoot) { this(peRoot, null, PETransformer.petEOS());}

	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		return new Instance().checkResult(parsing, sequence, pevs);
	}

	@Override
	public boolean checkFinal() { return true; }

}
