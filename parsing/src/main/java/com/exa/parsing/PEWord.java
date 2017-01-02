package com.exa.parsing;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.exa.buffer.CharReader.ClientBuffer;
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
	
	public PEWord() {
		super(PETransformer.petEOS());
		this.petFalse = PETransformer.petFAIL();
	}
	
	public PEWord(String ... strs) {
		super(PETransformer.petOK());
		this.petFalse = PETransformer.petFAIL();
		
		for(String str : strs) {
			requiredStrings.add(str.replace("\\n", "\n"));
		}
	}
	
	public void add(String str) { requiredStrings.add(str.replace("\\n", "\n")); }
	
	public int checkStringCount() { return requiredStrings.size(); }
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		ClientBuffer db = parsing.firstBufferizedRead();
		if(db == null) return EOS_FAIL;
		
		HashSet<String> strs = new HashSet<>();
		strs.addAll(requiredStrings);
		
		ClientBuffer db2 = parsing.bufferize();
		String strOK = null;
		Iterator<String> it = strs.iterator();
		
		String bufTrimValue = parsing.trimLeft(db);
		while(it.hasNext()) {
			String str = it.next();
			if(str.contains(bufTrimValue)) {
				if(str.equals(bufTrimValue)) strOK = str;
				continue;
			}
			it.remove();
		}
		
		ParsingEntity nextPE = getNextPE();
		
		if(strs.size() == 0) { 
			db.rewind().release();
			return notifyResult(parsing, petFalse.get(this, parsing, pevs), (ClientBuffer)null, pevs);
		}
		
		while(strs.size() > 0) {
			if(parsing.nextString() == null) {
				if(strOK == null) {
					db.rewind().release();
					return notifyResult(parsing, petFalse.get(this, parsing, pevs), (ClientBuffer)null, pevs);
				}
				
				db2.rewind().release();
				if(nextPE.isFinal())
					return notifyResult(parsing, PE_NEXT, db.release(), pevs);
				
				return notifyResult(parsing, nextPE, db.release(), pevs);
			}
			
			bufTrimValue = parsing.trimLeft(db);
			it = strs.iterator();
			while(it.hasNext()) {
				String str = it.next();
				if(str.contains(bufTrimValue)) {
					if(str.equals(bufTrimValue)) {
						db2.markPosition();
						strOK = str;
					}
					continue;
				}
				it.remove();
			}
		}
		
		if(strOK == null) {
			db.rewind().release();
			return notifyResult(parsing, petFalse.get(this, parsing, pevs), (ClientBuffer)null, pevs);
		}
		
		db2.rewind().release();
		
		if(nextPE.isFinal())
			return notifyResult(parsing, PE_NEXT, db.release(), pevs);
		
		return notifyResult(parsing, nextPE, db.release(), pevs);
	}

	public HashSet<String> getRequiredStrings() {
		return requiredStrings;
	}

	@Override
	public PEWord asPEWord() { return this;	}
	
}
