package com.exa.parsing;

import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.utils.ManagedException;

public class ParsingEntity {
	
	public static PEFail DEFAULT_FAIL = new PEFail();
	
	public static PEFail DEFAULT_FATAL_FAIL = new PEFail();
	
	public static PEFail EOS_FAIL = new PEFail();
	
	public static ParsingEntity EOS = new FinalPE();
	
	public static ParsingEntity OK = new FinalPE();
	
	public static ParsingEntity MISSING = new PEMissing();
	
	public final static ParsingEntity PE_REPEAT_END = new ParsingEntity();
	
	protected PETWithPE nextPET;
	
	protected boolean root = true;
	
	public ParsingEntity(PETWithPE nextPET) {
		this.nextPET = nextPET;
	}
	
	public ParsingEntity() { this(new PETIdentity(ParsingEntity.EOS)); }

	public boolean isFinal() { return false; }
	
	public boolean isPEMissing() { return false; }
		
	public PEFail asPEFail() { return null; }

	public boolean failed() { return asPEFail() != null; }
	
	public ParsingEntity peForNotify() { return this; }
	
	public PETWithPE getNextPET() {	return nextPET;	}

	public ParsingEntity getNextPE() { return nextPET.getPE(); }
	public ParsingEntity setNextPE(ParsingEntity nextPE) { 
		nextPET.setPE(nextPE);
		return nextPE;
	}
	
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException { 
		return DEFAULT_FAIL;
	}
	
	public ParsingEntity check(Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
		//if(parsing.nextString() == null) return Parsing.PE_BREAK;
		
		int sequence = parsing.newParsing();
		
		ParsingEntity res = checkResult(parsing, sequence, pevs);
		
		parsing.notifyEvent(pevs, peForNotify(), sequence, res);
		
		return res;
	}
	
	public boolean checkFinal() { return false; }
	
	public boolean isRoot() { return root; }

	public void setRoot(boolean root) {	this.root = root; }
	
	public DataBuffer firstBufferizeRead(Parsing<?> parsing) throws ManagedException {
		DataBuffer db = parsing.bufferize();
		if(parsing.nextString() == null) { db.release(); return null; }
		parsing.trimLeft(db);
		
		return db;
	}

}
