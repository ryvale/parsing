package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class PECharByChar extends PEAtomic {

	public PECharByChar(ParsingEntity peRoot, PETWithPE nextPET) {
		super(peRoot, nextPET);
	
	}
	
	public PECharByChar(ParsingEntity peRoot) {
		this(peRoot, PETransformer.petEOS());
	
	}

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		boolean cim = parsing.getCharIteratorMode();
		parsing.setCharIteratorMode(true);

		ParsingEntity res = super.checkResult(parsing, sequence, pevs);
		
		parsing.setCharIteratorMode(cim);
		
		return res;
	}

	@Override
	public PEAtomic asPEAtomic() { return null;	}
	
	
	
}
