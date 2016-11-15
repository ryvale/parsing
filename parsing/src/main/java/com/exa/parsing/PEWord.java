package com.exa.parsing;

import java.util.HashSet;
import java.util.List;

import com.exa.utils.ManagedException;

public class PEWord extends ParsingEntity {
	protected HashSet<String> requiredStrings = new HashSet<String>();
	protected PETransformer petFalse;
	
	protected PEWord(HashSet<String> requiredStrings, PETWithPE petOK, PETransformer petFalse) {
		super(petOK);
		this.requiredStrings = requiredStrings;
		this.petFalse = petFalse;
	}
	
	public PEWord(String kw, PETWithPE petOK, PETransformer petFalse) {
		super(petOK);
		requiredStrings.add(kw);
		this.petFalse = petFalse;
	}
	
	public PEWord(String kw, PETWithPE petOK) {
		this(kw, petOK, PETransformer.petFAIL());
	}
	
	public PEWord(String kw) {
		this(kw, PETransformer.petOK(), PETransformer.petFAIL());
	}
	
	public PEWord(String ... strs) {
		super(PETransformer.petOK());
		this.petFalse = PETransformer.petFAIL();
		
		for(String str : strs) {
			requiredStrings.add(str);
		}
	}
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		if(requiredStrings.contains(parsing.currentWord())) return nextPET.get(null, parsing, pevs);
		
		return petFalse.get(null, parsing, pevs);
	}
}
