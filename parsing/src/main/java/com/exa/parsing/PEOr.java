package com.exa.parsing;

import java.util.ArrayList;
import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.utils.ManagedException;

public class PEOr extends ParsingEntity {
	
	class InstanceSpecificData {
		ParsingEntity orBranchPE;
		List<ParsingEvent> pevs;
		ParsingEntity currentPE;
		String str = null;
		
		public InstanceSpecificData(ParsingEntity orBranchPE) {
			super();
			this.orBranchPE = orBranchPE;
			currentPE = orBranchPE;
			pevs = new ArrayList<>();
		}
		
		ParsingEntity check(Parsing<?> parsing) throws ManagedException {
			return currentPE = currentPE.checkBranch(parsing, pevs);
		}
		
		public void reset() {
			pevs.clear();
			currentPE = orBranchPE;
		}
	}
	
	protected List<ParsingEntity> pes;
	
	public PEOr(List<ParsingEntity> pes, PETWithPE nextPET) {
		super(nextPET);
		this.pes = pes;
	}

	public PEOr() {
		this(new ArrayList<ParsingEntity>(), PETransformer.petEOS());
	}
	
	public PEOr add(ParsingEntity pe) { pes.add(pe); pe.setRoot(false); return this; }
	
	public PEOr add(String str) { return add(new PEWord(str)); }

	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		DataBuffer db = parsing.bufferize();
		
		int nb = 0;
		InstanceSpecificData isdOK = null;
		for(ParsingEntity pe : pes) {
			InstanceSpecificData isd = new InstanceSpecificData(pe);
			
			ParsingEntity currentPE = isd.check(parsing);
			while(!currentPE.isFinal()) currentPE = isd.check(parsing);
			if(!currentPE.failed() || currentPE == PE_NEXT_CHECK) {
				isd.str = db.value();
				if(nb == 0 || isd.str.length()>isdOK.str.length()) {isdOK = isd; nb = 1;}
				else if(isd.str.length() == isdOK.str.length()) ++nb;
			}
			db.rewind();
		}
		
		
		
		if(nb == 0) return new PEFail("No parsing entity can parse in a or");
		
		if(nb > 1) return new PEFail("Too many parsing entity can parse in a or");
		
		isdOK.reset();
		isdOK.check(parsing);
		pevs.addAll(isdOK.pevs);
		
		if(isdOK.currentPE == PE_NEXT_CHECK) {
			return nextPET.getPE().check(parsing, pevs);
		}
		
		return nextPET.get(isdOK.currentPE, parsing, pevs);

	}

}
