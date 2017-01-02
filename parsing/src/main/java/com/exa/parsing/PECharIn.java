package com.exa.parsing;

import java.util.List;

import com.exa.buffer.CharReader.ClientBuffer;
import com.exa.utils.ManagedException;

public class PECharIn extends ParsingEntity {
	protected char l, u;

	public PECharIn(char l, char u, PETWithPE nextPET) {
		super(nextPET);
		this.l = l;
		this.u = u;
	}
	
	public PECharIn(char l, char u) { this(l, u, PETransformer.petEOS()); }

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		if(!parsing.hasNextString()) return EOS_FAIL;
		
		ClientBuffer cb = parsing.bufferize();
		
		char c = parsing.nextChar();
		
		if(c<l) {
			cb.rewind().release();
			return notifyResult(parsing, new PEFail("Invalid character. expected character between " + l +" and " + u), pevs)  ;
		}
		
		if(c>u) {
			cb.rewind().release();
			return notifyResult(parsing, new PEFail("Invalid character. expected character between " + l +" and " + u), pevs)  ;
		}
		
		return notifyResult(parsing, nextPET.get(null, parsing, pevs), cb.release(), pevs);
	}
	
}
