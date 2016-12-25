package com.exa.parsing;

import java.util.List;

import com.exa.buffer.CharReader.ClientBuffer;
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
		ClientBuffer db = parsing.firstBufferizedRead();
		if(db == null) return EOS_FAIL;
		
		if(perValid.isOK(this, parsing)) {
			//db.release();
			return notifyResult(parsing, nextPET.get(this, parsing, pevs), db.release(), pevs); //notifyResult(parsing, result, lpevs, pevs); // notifyReult(parsing, nextPET.get(this, parsing, pevs), db.release(), pevs);
		}
		
		db.rewind().release();
		return notifyResult(parsing, petFalse.get(this, parsing, pevs), (ClientBuffer)null, pevs);//notifyReult(parsing, petFalse.get(this, parsing, pevs), (String)null, pevs);
	}
	
	

}
