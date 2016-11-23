package com.exa.parsing;

import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.utils.ManagedException;

public class PEUntilNextString extends ParsingEntity {
	protected String seekString;
	protected boolean mandatory, include;

	public PEUntilNextString(String seekString, boolean mandatory, boolean include, PETWithPE nextPET) {
		super(nextPET);
		this.seekString = seekString;
		this.mandatory = mandatory;
		this.include = include;
	}
	
	public PEUntilNextString(String seekString, boolean mandatory, boolean include) {
		this(seekString, mandatory, include, PETransformer.petEOS());
		this.seekString = seekString;
		this.mandatory = mandatory;
		this.include = include;
	}

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		DataBuffer db = parsing.firstBufferizeRead();
		if(db == null) return EOS_FAIL;
		
		int nb = 0;
		do {
			++nb;
			if(seekString.equals(parsing.lexerWord())) {
				if(include)	 {
					return notifyResult(parsing, nextPET.get(this, parsing, pevs), db.release(), pevs);
				}
				db.rewind();
				
				for(int i=1; i<nb; i++) parsing.nextString();
				
				return notifyResult(parsing, nextPET.get(this, parsing, pevs), db.release(), pevs);
			}
		}
		while(parsing.nextString() != null);
		
		if(mandatory) {
			db.rewindAndRelease();
			return notifyResult(parsing, new PEFail("Unable to find '"+ seekString+"' in the expression."), (String)null, pevs); //new PEFail("Unable to find '"+ seekString+"' in the expression.");
		}
		
		return notifyResult(parsing, nextPET.get(this, parsing, pevs), db.release(), pevs);
	}
	
	
}
