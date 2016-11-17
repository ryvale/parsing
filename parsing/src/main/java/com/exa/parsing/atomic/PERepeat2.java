package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.PETWithPE;
import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class PERepeat2 extends PEAtomic {
	
	class Instance extends com.exa.parsing.atomic.Instance {
		protected int execCount = 0;

		public Instance() {
			super(PERepeat2.this, peRoot);
		}

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			ParsingEntity res = super.nextIteration(parsing, sequence, pevs);
			
			if(res.failed() || res == PE_NEXT_CHECK) {	
				if(execCount >= min) {
					if(PERepeat2.this.nextPET.getPE().isFinal()) {
						if(PERepeat2.this.isRoot()) return res;
						
						return PE_NEXT_CHECK;
					}
					
					parsing.registerResult(sequence, res == PE_NEXT_CHECK ? DEFAULT_FAIL : res);
					return PERepeat2.this.nextPET.getPE().check(parsing, pevs);
				}
				
				return DEFAULT_FAIL;
			}
			
			while(!res.isFinal() && res != PE_NEXT_CHECK) res = super.nextIteration(parsing, sequence, pevs);
			
			if(res.failed() || res == PE_NEXT_CHECK) {
				if(execCount >= min) {
					if(PERepeat2.this.nextPET.getPE().isFinal()) {
						if(PERepeat2.this.isRoot()) return res;
						
						return PE_NEXT_CHECK;
					}
					
					parsing.registerResult(sequence, res == PE_NEXT_CHECK ? DEFAULT_FAIL : res);
					return PERepeat2.this.nextPET.getPE().check(parsing, pevs);
				}
				
				return DEFAULT_FAIL;
			}
			
			currentPE = peRoot;
			
			execCount++;
			
			return this;
		}
		
		@Override
		public boolean checkFinal() {
			if(execCount>=min) {
				if(currentPE == peRoot) return true;
			}
			
			return currentPE.checkFinal();
		}
	}
	
	protected int min = 0;

	public PERepeat2(ParsingEntity peRoot, int min, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(peRoot, instancePEForNotify, nextPET);
		
		this.min = min;
	}
	
	public PERepeat2(ParsingEntity peRoot, int min) { this(peRoot, min, null, PETransformer.petEOS()); }
	
	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		return new Instance().checkResult(parsing, sequence, pevs);
	}
	
	@Override
	public boolean checkFinal() { return min == 0; }

}
