package com.exa.parsing;

import java.util.List;

import com.exa.lexing.CharReader.Buffer;
import com.exa.utils.ManagedException;

public class ParsingEntity {
	
	public final static PEFail DEFAULT_FAIL = new PEFail();
	
	public final static PEFail DEFAULT_FATAL_FAIL = new PEFail();
	
	public final static PEFail EOS_FAIL = new PEFail();
	
	public final static ParsingEntity EOS = new FinalPE();
	
	public final static ParsingEntity OK = new FinalPE();
	
	public final static ParsingEntity PE_NEXT = new FinalPE();
	
	public final static ParsingEntity PE_NEXT_CHECK = new PEFail();
	
	public final static ParsingEntity PE_REPEAT_END = new ParsingEntity();
	
	public final static ParsingEntity PE_NULL = new ParsingEntity();
	
	protected PETWithPE nextPET;
	
	protected boolean root = true;
	
	public ParsingEntity(PETWithPE nextPET) {
		this.nextPET = nextPET;
	}
	
	public ParsingEntity() { this(new PETIdentity(ParsingEntity.EOS)); }

	public boolean isFinal() { return false; }
	
	//public boolean isPEMissing() { return false; }
		
	public PEFail asPEFail() { return null; }
	
	public PEAtomic asPEAtomic() { return null; }

	public boolean failed() { return asPEFail() != null; }
	
	public ParsingEntity peForNotify() { return this; }
	
	public PETWithPE getNextPET() {	return nextPET;	}

	public ParsingEntity getNextPE() { return nextPET.getPE(); }
	public ParsingEntity setNextPE(ParsingEntity nextPE) { 
		nextPET.setPE(nextPE);
		
		if(nextPE.isFinal()) return nextPE;
		
		nextPE.setRoot(root);
		
		return nextPE;
	}
	
	public ParsingEntity setNextPE(String word) {
		return setNextPE(new PEWord(word));
	}
	
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException { 
		return DEFAULT_FAIL;
	}
	
	public ParsingEntity check(Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
		int sequence = parsing.newParsing();
		
		ParsingEntity res = checkResult(parsing, sequence, pevs);
		
		return res;
	}
	
	public boolean checkFinal() { return false; }
	
	public boolean isRoot() { return root; }

	public void setRoot(boolean root) {	
		this.root = root;
		
		ParsingEntity nextPE = getNextPE();
		if(nextPE.isFinal()) return;
		nextPE.setRoot(root);
	}
		
	public ParsingEntity checkBranch(Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
		ParsingEntity currentPE = this;
	
		Buffer db = parsing.bufferize();
		
		do { currentPE = currentPE.check(parsing, pevs); } while(!currentPE.isFinal());
		
		if(currentPE != PE_NEXT_CHECK && currentPE.failed()) db.rewindAndRelease();
		else db.release();
		
		return currentPE;
	}
	
	public ParsingEntity standardReseultInterpretation(ParsingEntity currentPE, Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		if(currentPE.failed()) {
			if(currentPE == PE_NEXT_CHECK) {
				ParsingEntity nextPE = getNextPE();
				if(nextPE.isFinal()) {
					if(isRoot()) return DEFAULT_FAIL;
					
					return PE_NEXT_CHECK;
				}
				
				return nextPE.check(parsing, pevs);
			}
			
			return currentPE;
		}
		
		if(currentPE == PE_NEXT) {
			ParsingEntity nextPE = getNextPE();
			if(nextPE.isFinal()) {
				if(isRoot()) return EOS;
				
				return PE_NEXT;
			}
			
			return nextPE;
		}
		
		return currentPE;
	}
	
	protected ParsingEntity notifyResult(Parsing<?> parsing, ParsingEntity result, List<ParsingEvent> lpevs, List<ParsingEvent> pevs) throws ManagedException {
		
		parsing.notifyEvent(pevs, this, lpevs, result);
		pevs.addAll(lpevs);
		
		return result;
	}
	
	protected ParsingEntity notifyResult(Parsing<?> parsing, ParsingEntity result, Buffer buffer, List<ParsingEvent> pevs) throws ManagedException {
		parsing.notifyEvent(pevs, this, buffer, result);
		
		return result;
	}
	
	protected ParsingEntity notifyResult(Parsing<?> parsing, ParsingEntity result, Buffer buffer, List<ParsingEvent> lpevs, List<ParsingEvent> pevs) throws ManagedException {
		parsing.notifyEvent(pevs, this, buffer, result);
		
		return result;
	}
	
	protected ParsingEntity notifyResult(Parsing<?> parsing, ParsingEntity result, List<ParsingEvent> pevs) throws ManagedException {
		parsing.notifyEvent(pevs, this, (Buffer)null, result);
		
		return result;
	}
	
	
	public PEWord asPEWord() { return null; }
}
