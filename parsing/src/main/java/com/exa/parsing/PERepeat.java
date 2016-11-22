package com.exa.parsing;

import java.util.ArrayList;
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
		if(peRoot.getNextPE() == EOS) peRoot.setNextPE(PE_NEXT);
	}
	
	public PERepeat(ParsingEntity peRoot, int min) { this(peRoot, min, PETransformer.petEOS());}

	public PERepeat(ParsingEntity peRoot) { this(peRoot, 0, PETransformer.petEOS()); }
	
	public PERepeat(String wrd, int min) { this(new PEWord(wrd), min); }
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		ParsingEntity currentPE;
		int execCount = 0;
		List<ParsingEvent> lpevs = new ArrayList<>();

		do {
			currentPE = peRoot.checkBranch(parsing, lpevs);
			if(currentPE.failed()) {
				
				if(currentPE == PE_NEXT_CHECK) continue;
				
				ParsingEntity nextPE = getNextPE();
				if(execCount >= min) {
					
					if(nextPE.isFinal()) {
						if(isRoot()) return notifyResult(parsing, EOS, lpevs, pevs);
						
						return notifyResult(parsing, PE_NEXT_CHECK, lpevs, pevs);
					}
					
					notifyResult(parsing, PE_NEXT_CHECK, lpevs, pevs);
					return nextPE.check(parsing, pevs);
				}
				
				/*if(currentPE == PE_NEXT_CHECK) {
					if(nextPE.isFinal()) {
						if(isRoot()) return DEFAULT_FAIL;
						
						return notifyResult(parsing, PE_NEXT_CHECK, lpevs, pevs);
					}
					
					parsing.registerResult(sequence, DEFAULT_FAIL);
					return nextPE.check(parsing, pevs);
				}*/
				
				return notifyResult(parsing, currentPE, lpevs, pevs);
			}
			
			++execCount;
		} while(currentPE != EOS_FAIL && currentPE != EOS);
		
		return notifyResult(parsing, currentPE, lpevs, pevs);
	}

	@Override
	public boolean checkFinal() { return min == 0; }
	
	protected ParsingEntity notifyResult(Parsing<?> parsing, ParsingEntity result, List<ParsingEvent> lpevs, List<ParsingEvent> pevs) throws ManagedException {
		int nb = 0;
		for(ParsingEvent pev : lpevs) {
			if(pev.getWord() == null) ++nb;
		}
		
		if(parsing.notifyEvent(pevs, this, nb, result))
			pevs.addAll(lpevs);
		
		return result;
	}
}
