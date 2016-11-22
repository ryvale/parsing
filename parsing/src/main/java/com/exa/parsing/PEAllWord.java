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
			db.rewindAndRelease();
			return notifyReult(parsing, new PEFail("Unexpected end of file."), db.release(), pevs); //new PEFail("Unexpected end of file.");
		}
		
		return notifyReult(parsing, nextPET.get(null, parsing, pevs), db.release(), pevs);
	}
	
	protected ParsingEntity notifyReult(Parsing<?> parsing, ParsingEntity result, String word, List<ParsingEvent> pevs) throws ManagedException {
		parsing.notifyEvent(pevs, this, word, result);
		return result;
	}
}
