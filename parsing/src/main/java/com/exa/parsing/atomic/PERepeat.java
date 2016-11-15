package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.FinalPE;
import com.exa.parsing.PETWithPE;
import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class PERepeat extends PEAtomic {
	public final static FinalPE PE_RESTART = new FinalPE();
	public final static FinalPE PE_EXIT = new FinalPE();
	
	class Instance extends com.exa.parsing.atomic.Instance {
		
		public Instance() {
			super(PERepeat.this, peRoot);
		}

		protected int execCount = 0;

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			ParsingEntity res = super.nextIteration(parsing, sequence, pevs);
						
			if(res.asPEFail() == null && res != PE_NEXT_CHECK) {
				
				if(res == PE_RESTART) {
					execCount++;
					currentPE = peRoot;
				}
				
				if(res.isFinal()) {
					execCount++;
					currentPE = peRoot;
					
					parsing.registerResult(sequence, PE_REPEAT_END);
				}
				return this;
			} 
			
			currentPE = peRoot;
			ParsingEntity res2 = super.nextIteration(parsing, sequence, pevs);
			if(res2.asPEFail() == null && res2 != PE_NEXT_CHECK) {
				res = res2;
				execCount++;
				
				return this;
			}
			
			if(execCount >= min) {
				if(PERepeat.this.nextPET.getPE().isFinal()) {
					if(PERepeat.this.isRoot()) return res;
					return PE_NEXT_CHECK;
				}
				parsing.registerResult(sequence, res2 == PE_NEXT_CHECK ? DEFAULT_FAIL : res2);
				return PERepeat.this.nextPET.getPE().check(parsing, pevs);
			}
			
			return res;
		}

		
		@Override
		public boolean checkFinal() {
			if(execCount>=min) {
				if(currentPE == peRoot) return true;
			}
			
			return currentPE.checkFinal();
		}
		
	}
	
	protected int min;

	public PERepeat(ParsingEntity peRoot, int min, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(peRoot, instancePEForNotify, nextPET);
		
		this.min = min;
	}
	
	public PERepeat(ParsingEntity peRoot, int min) { this(peRoot, min, null, PETransformer.petEOS()); }
	
	public PERepeat(ParsingEntity peRoot) { this(peRoot, 0, null, PETransformer.petEOS()); }

	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		return new Instance().checkResult(parsing, sequence, pevs);
	}

	@Override
	public boolean checkFinal() { return min == 0; }
	
	
	
}
