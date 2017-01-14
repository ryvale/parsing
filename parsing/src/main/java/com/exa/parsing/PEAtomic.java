package com.exa.parsing;

import java.util.ArrayList;
import java.util.List;

import com.exa.buffer.CharReader.ClientBuffer;
import com.exa.utils.ManagedException;

public class PEAtomic extends ParsingEntity {
	protected ParsingEntity peRoot;
	
	public PEAtomic(ParsingEntity peRoot, PETWithPE nextPET) {
		super(nextPET);
		this.peRoot = peRoot;
		if(peRoot == null) return;
		peRoot.setRoot(false);
	}
	
	public PEAtomic(ParsingEntity peRoot) { this(peRoot, PETransformer.petEOS()); }
	
	public PEAtomic(ParsingEntity peRoot, ParsingEntity nextPE) { this(peRoot, new PETIdentity(nextPE)); }

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		ParsingEntity currentPE = peRoot;
		int exec = 0;
		
		List<ParsingEvent> lpevs = new ArrayList<>();
		
		ClientBuffer db = parsing.bufferize();
		while(!currentPE.isFinal()) {
			currentPE = currentPE.check(parsing, lpevs);
			++exec;
		}
		
		if(currentPE != PE_NEXT_CHECK && currentPE.failed()) db.rewind().release(); else db.release();
		
		if(currentPE.failed()) {
			if(currentPE == PE_NEXT_CHECK) {
				ParsingEntity nextPE = getNextPE();
				if(nextPE.isFinal()) {
					if(isRoot()) return notifyResult(parsing, DEFAULT_FAIL, lpevs, pevs);
					
					notifyResult(parsing, exec>1 ? OK : PE_NEXT_CHECK, lpevs, pevs);
					
					return PE_NEXT_CHECK;
				}
				
				notifyResult(parsing, OK, lpevs, pevs);
				return nextPE.check(parsing, pevs);
			}
			
			return notifyResult(parsing, currentPE, lpevs, pevs);
		}
		
		/*ParsingEntity nextPE = getNextPE();
		if(currentPE == PE_NEXT) {
			if(nextPE.isFinal()) {
				if(isRoot()) return notifyResult(parsing, EOS, lpevs, pevs);
				
				return notifyResult(parsing, PE_NEXT, lpevs, pevs);
			}
			
			return notifyResult(parsing, nextPE, lpevs, pevs);
		}*/
		
		ParsingEntity nextPE = getNextPE();
		//if(currentPE == PE_NEXT) {
		if(nextPE.isFinal()) {
			if(isRoot()) return notifyResult(parsing, EOS, lpevs, pevs);
			
			return notifyResult(parsing, PE_NEXT, lpevs, pevs);
		}
			
			//return notifyResult(parsing, nextPE, lpevs, pevs);
		//}
		
		return notifyResult(parsing, nextPE, lpevs, pevs);
	}

	@Override
	public PEAtomic asPEAtomic() { return this;	}

	public void setPERoot(ParsingEntity peRoot) {
		this.peRoot = peRoot;
		peRoot.setRoot(false);
	}

	@Override
	public boolean checkFinal() {
		return peRoot.checkFinal();
	}
	
	
	
}
