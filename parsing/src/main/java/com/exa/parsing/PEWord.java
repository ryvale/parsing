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
		DataBuffer db = parsing.firstBufferizeRead();
		if(db == null) return EOS_FAIL; 
		
		HashSet<String> strs = new HashSet<>();
		strs.addAll(requiredStrings);
		
		DataBuffer db2 = parsing.bufferize();
		String strOK = null;
		Iterator<String> it = strs.iterator();
		while(it.hasNext()) {
			String str = it.next();
			if(str.contains(db.value())) {
				if(str.equals(db.value())) strOK = str;
				continue;
			}
			it.remove();
		}
		
		if(strs.size() == 0) { 
			db.rewindAndRelease();
			return petFalse.get(this, parsing, pevs);
		}
		
		while(strs.size() > 0) {
			if(parsing.nextString() == null) {
				if(strOK == null) {
					db.rewindAndRelease();
					return petFalse.get(this, parsing, pevs);
				}
				
				db2.rewindAndRelease();
				db.release();
				return nextPET.get(this, parsing, pevs);
			}
			
			it = strs.iterator();
			while(it.hasNext()) {
				String str = it.next();
				if(str.contains(db.value())) {
					if(str.equals(db.value())) {
						db2.markPosition();
						strOK = str;
					}
					continue;
				}
				it.remove();
			}
		}
		
		if(strOK == null) {
			db.rewindAndRelease();
			return petFalse.get(this, parsing, pevs);
		}
		
		/*String str = strs.iterator().next();
		while(str.contains(db.value())) {
			if(str.equals(db.value())) {
				db.release();
				return nextPET.get(this, parsing, pevs);
			}
			
			if(parsing.nextString() == null) break;
		}*/
	
		db2.rewindAndRelease();
		db.release();
		return nextPET.get(this, parsing, pevs);
	}
}
