package com.exa.parsing;

import java.util.ArrayList;
import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.utils.ManagedException;

public class PEAtomic extends ParsingEntity {
	protected ParsingEntity peRoot;
	
	public PEAtomic(ParsingEntity peRoot, PETWithPE nextPET) {
		super(nextPET);
		this.peRoot = peRoot;
		peRoot.setRoot(false);
	}
	
	public PEAtomic(ParsingEntity peRoot) { this(peRoot, PETransformer.petEOS()); }
	
	public PEAtomic(ParsingEntity peRoot, ParsingEntity nextPE) { this(peRoot, new PETIdentity(nextPE)); }

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		ParsingEntity currentPE = peRoot;
		
		List<ParsingEvent> lpevs = new ArrayList<>();
		
		DataBuffer db = parsing.monitorCharReading(false);
		while(!currentPE.isFinal()) 
			currentPE = currentPE.check(parsing, lpevs);
		
		if(currentPE != PE_NEXT_CHECK && currentPE.failed()) db.rewindAndRelease(); else db.release();
		
		if(currentPE.failed()) {
			if(currentPE == PE_NEXT_CHECK) {
				ParsingEntity nextPE = getNextPE();
				if(nextPE.isFinal()) {
					if(isRoot()) return notifyResult(parsing, DEFAULT_FAIL, lpevs, pevs);
					
					return notifyResult(parsing, PE_NEXT_CHECK, lpevs, pevs);
				}
				
				notifyResult(parsing, OK, lpevs, pevs);
				return nextPE.check(parsing, pevs);
			}
			
			return notifyResult(parsing, currentPE, lpevs, pevs);
		}
		
		if(currentPE == PE_NEXT) {
			ParsingEntity nextPE = getNextPE();
			if(nextPE.isFinal()) {
				if(isRoot()) return notifyResult(parsing, EOS, lpevs, pevs);
				
				return notifyResult(parsing, PE_NEXT, lpevs, pevs);
			}
			
			return notifyResult(parsing, nextPE, lpevs, pevs);
		}
		
		return notifyResult(parsing, currentPE, lpevs, pevs);
	}
	
}
