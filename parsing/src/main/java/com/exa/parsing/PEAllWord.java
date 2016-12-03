package com.exa.parsing;

import java.util.List;

import com.exa.lexing.CharReader.Buffer;
import com.exa.utils.ManagedException;

public class PEAllWord extends ParsingEntity {
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		Buffer db = parsing.firstBufferizedRead();
		if(db == null) return EOS_FAIL;
		
		if(parsing.lexerWord() == null) {
			db.rewindAndRelease();
			return notifyResult(parsing, new PEFail("Unexpected end of file."), (String)null, pevs); //new PEFail("Unexpected end of file.");
		}
		
		return notifyResult(parsing, nextPET.get(null, parsing, pevs), db.release(), pevs);
	}
	
}
