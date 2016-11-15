package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class PEAllWord extends ParsingEntity {

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		if(parsing.currentWord() == null) return new PEFail("Unexpected end of file.");
		
		return nextPET.get(null, parsing, pevs);
	}
	
	

}
