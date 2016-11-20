package com.exa.parsing;

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
		
		ParsingEntity currentPE = peRoot.checkBranch(parsing, pevs);
		if(currentPE.failed()) {
			ParsingEntity currentPE2 = getNextPE();
			
			if(currentPE2.isFinal()) {  
				if(isRoot()) return DEFAULT_FAIL;
				
				return PE_NEXT_CHECK;	
			}
			parsing.registerResult(sequence, currentPE);
			
			currentPE2 = currentPE2.check(parsing, pevs);
			if(currentPE2.failed()) return currentPE;
			
			return currentPE2;
		}
		
		return nextPET.get(currentPE, parsing, pevs);
	}

	@Override
	public boolean checkFinal() {
		ParsingEntity npe = getNextPE();
		if(npe.failed()) return false;
		
		if(npe.isFinal()) return true;
		
		return false;
	}
	
	
	
}
