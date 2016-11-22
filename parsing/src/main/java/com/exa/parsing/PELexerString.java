package com.exa.parsing;

import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.utils.ManagedException;

public class PELexerString extends ParsingEntity {
	protected PERule perValid;
	protected PETransformer petFalse;

	public PELexerString(PERule perValid, PETWithPE petOK, PETransformer petFalse) {
		super(petOK);
		this.perValid = perValid;
		this.petFalse = petFalse;
	}
	
	public PELexerString(PERule perValid) { this(perValid, PETransformer.petOK(), PETransformer.petFAIL());}

	public PELexerString(PERule perOK, PETransformer petFail) {
		this(perOK, PETransformer.petOK(), petFail);
	}

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		DataBuffer db = parsing.firstBufferizeRead();
		if(db == null) return EOS_FAIL;
		
		if(perValid.isOK(this, parsing)) {
			//db.release();
			return notifyReult(parsing, nextPET.get(this, parsing, pevs), db.release(), pevs);
		}
		
		db.rewindAndRelease();
		return notifyReult(parsing, petFalse.get(this, parsing, pevs), null, pevs); //petFalse.get(this, parsing, pevs);
	}
	
	protected ParsingEntity notifyReult(Parsing<?> parsing, ParsingEntity result, String word, List<ParsingEvent> pevs) throws ManagedException {
		parsing.notifyEvent(pevs, this, word, result);
		return result;
	}

}
