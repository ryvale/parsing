package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class PETCheck extends PETWithPE {

	public PETCheck(ParsingEntity pe) {
		super(pe);
	}
	
	public PETCheck() {	this(null);	}

	@Override
	public ParsingEntity get(ParsingEntity currentPE, Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
		return pe.check(parsing, pevs);
	}

}
