package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class PETIdentity extends PETWithPE {

	public PETIdentity(ParsingEntity pe) {
		super(pe);
	}

	@Override
	public ParsingEntity get(ParsingEntity currentPE, Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
		return pe;
	}
	
}
