package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class PERepeat extends ParsingEntity {
	protected int min;
	protected ParsingEntity peRoot;

	public PERepeat(ParsingEntity peRoot, int min, PETWithPE nextPET) {
		super(nextPET);
		this.min = min;
		this.peRoot = peRoot;
		peRoot.setRoot(false);
	}
	
	public PERepeat(ParsingEntity peRoot, int min) { this(peRoot, min, PETransformer.petEOS());}

	public PERepeat(ParsingEntity peRoot) { this(peRoot, 0, PETransformer.petEOS()); }
	
	public PERepeat(String wrd, int min) { this(new PEWord(wrd), min); }
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		ParsingEntity currentPE;
		int execCount = 0;
		
		do {
			currentPE = peRoot.check(parsing, pevs);
			
			if(currentPE.failed()) {
				ParsingEntity nextPE = getNextPE();
				if(execCount >= min) {
					
					if(nextPE.isFinal()) {
						if(isRoot()) return EOS;
						
						return PE_NEXT_CHECK;
					}
					
					parsing.registerResult(sequence, DEFAULT_FAIL);
					return nextPE.check(parsing, pevs);
				}
				
				if(currentPE == PE_NEXT_CHECK) {
					if(nextPE.isFinal()) {
						if(isRoot()) return DEFAULT_FAIL;
						
						return PE_NEXT_CHECK;
					}
					
					parsing.registerResult(sequence, DEFAULT_FAIL);
					return nextPE.check(parsing, pevs);
				}
				
				
				return currentPE;
			}
			
			while(!currentPE.isFinal()) currentPE = currentPE.check(parsing, pevs);
			currentPE = standardReseultInterpretation(currentPE, parsing, sequence, pevs);
			if(currentPE.failed()) return currentPE;
			++execCount;
			
			
		} while(currentPE != EOS_FAIL && currentPE != EOS);
		
		
		
		return currentPE;
	}

	@Override
	public boolean checkFinal() { return min == 0; }
	
	
}
