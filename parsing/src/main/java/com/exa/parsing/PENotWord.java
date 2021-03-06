package com.exa.parsing;

import java.util.HashSet;
import java.util.List;

import com.exa.buffer.CharReader.ClientBuffer;
import com.exa.utils.ManagedException;

public class PENotWord extends ParsingEntity {
	protected HashSet<String> requiredStrings = new HashSet<String>();
	protected PETransformer petFalse;
	
	protected PENotWord(HashSet<String> requiredStrings, PETWithPE petOK, PETransformer petFalse) {
		super(petOK);
		this.requiredStrings = requiredStrings;
		this.petFalse = petFalse;
	}
	
	public PENotWord(String kw, PETWithPE petOK, PETransformer petFalse) {
		super(petOK);
		requiredStrings.add(kw);
		this.petFalse = petFalse;
	}
	
	public PENotWord(HashSet<String> requiredStrings) {
		super(PETransformer.petEOS());
		this.requiredStrings.addAll(requiredStrings);
		this.petFalse = PETransformer.petFAIL();
	}
	
	public PENotWord(String kw, PETWithPE petOK) {
		this(kw, petOK, PETransformer.petFAIL());
	}
	
	public PENotWord(String kw) {
		this(kw, PETransformer.petOK(), PETransformer.petFAIL());
	}
	
	public PENotWord(String ... strs) {
		super(PETransformer.petOK());
		this.petFalse = PETransformer.petFAIL();
		
		for(String str : strs) {
			requiredStrings.add(str);
		}
	}
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		ClientBuffer db = parsing.firstBufferizedRead();
		if(db == null) return EOS_FAIL;
		
		if(requiredStrings.contains(parsing.lexerWord())) {
			db.rewind().release();
			return notifyResult(parsing, petFalse.get(null, parsing, pevs), pevs);
		}
		
		return notifyResult(parsing, nextPET.get(null, parsing, pevs), db.release(), pevs);
	}
	
}
