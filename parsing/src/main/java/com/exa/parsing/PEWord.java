package com.exa.parsing;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
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
		DataBuffer db = firstBufferizeRead(parsing);
		if(db == null) return EOS_FAIL; 
		
		HashSet<String> strs = new HashSet<>();
		strs.addAll(requiredStrings);
		
		Iterator<String> it = strs.iterator();
		while(it.hasNext()) {
			String str = it.next();
			if(str.contains(db.value())) continue;
			it.remove();
		}
		
		if(strs.size() == 0) {
			db.rewind();
			return petFalse.get(null, parsing, pevs);
		}
		
		while(strs.size() > 1) {
			if(parsing.nextString() == null) {
				db.rewind();
				return petFalse.get(null, parsing, pevs);
			}
			
			it = strs.iterator();
			while(it.hasNext()) {
				String str = it.next();
				if(str.contains(db.value())) continue;
				it.remove();
			}
		}
		
		String str = strs.iterator().next();
		while(str.contains(db.value())) {
			if(str.equals(db.value())) return nextPET.get(null, parsing, pevs);
			
			if(parsing.nextString() == null) break;
		}
	
		db.rewind();
		return petFalse.get(null, parsing, pevs);
	}
}
