package com.exa.parsing;

import java.util.ArrayList;
import java.util.List;

import com.exa.utils.ManagedException;

public class PEOptional extends ParsingEntity {
	protected ParsingEntity peRoot;

	public PEOptional(ParsingEntity peRoot, PETWithPE nextPET) {
		super(nextPET);
		this.peRoot = peRoot;
		peRoot.setRoot(false);
		
	}
	
	public PEOptional(ParsingEntity peRoot) { this(peRoot, PETransformer.petEOS());}
	
	public PEOptional(String word) { this(new PEWord(word), PETransformer.petEOS()); }
	
	public PEOptional(String ... words) { this(new PEWord(words), PETransformer.petEOS()); }
	

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		if(!parsing.hasNextString()) return EOS;
		
		ParsingEntity nextPE = getNextPE();
		
		List<ParsingEvent> lpevs = new ArrayList<>();
		ParsingEntity currentPE = peRoot.checkBranch(parsing, lpevs);
		if(currentPE.failed()) {
			ParsingEntity currentPE2 = nextPE;
			
			if(currentPE2.isFinal()) {  
				if(isRoot()) return notifyResult(parsing, DEFAULT_FAIL, lpevs, pevs);
				
				return notifyResult(parsing, PE_NEXT_CHECK, lpevs, pevs);	
			}
			
			currentPE2 = currentPE2.check(parsing, lpevs);
			if(currentPE2.failed()) return notifyResult(parsing, currentPE, lpevs, pevs);
			
			return notifyResult(parsing, currentPE2, lpevs, pevs);
		}
		
		if(nextPE == EOS) {
			if(!isRoot()) return notifyResult(parsing,PE_NEXT, lpevs, pevs);
		}
		
		return notifyResult(parsing, nextPET.get(currentPE, parsing, lpevs), lpevs, pevs);
	}

	@Override
	public boolean checkFinal() {
		ParsingEntity npe = getNextPE();
		if(npe.failed()) return false;
		
		if(npe.isFinal()) return true;
		
		return false;
	}
}
