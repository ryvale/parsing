package com.exa.parsing;

import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.utils.ManagedException;

public class PEAllWord extends ParsingEntity {
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		DataBuffer db = parsing.firstBufferizeRead();
		if(db == null) return EOS_FAIL;
		
		if(parsing.lexerWord() == null) {
			db.release();
			return new PEFail("Unexpected end of file.");
		}
		
		return nextPET.get(null, parsing, pevs);
	}
	

}
