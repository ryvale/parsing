package com.exa.parsing;

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
		
		DataBuffer db = parsing.bufferize();
		while(!currentPE.isFinal()) 
			currentPE = currentPE.check(parsing, pevs);
		
		if(currentPE != PE_NEXT_CHECK && currentPE.failed()) db.rewindAndRelease(); else db.release();
		
		currentPE = standardReseultInterpretation(currentPE, parsing, sequence, pevs);
		
		return currentPE;
	}
	
}
