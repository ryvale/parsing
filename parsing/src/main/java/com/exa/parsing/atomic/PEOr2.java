package com.exa.parsing.atomic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.parsing.PEFail;
import com.exa.parsing.PETWithPE;
import com.exa.parsing.PETransformer;
import com.exa.parsing.PEWord;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class PEOr2 extends PEAtomic {
	
	class InstanceSpecificData {
		ParsingEntity orBranchPE;
		List<ParsingEvent> pevs;
		ParsingEntity currentPE;
		String str = null;
		int number = 0;
		
		public InstanceSpecificData(ParsingEntity orBranchPE) {
			super();
			this.orBranchPE = orBranchPE;
			currentPE = orBranchPE;
			pevs = new ArrayList<>();
		}
		
		ParsingEntity check(Parsing<?> parsing) throws ManagedException {
			++number;
			return currentPE = currentPE.check(parsing, pevs);
		}
		
		public void reset() { currentPE = orBranchPE; number = 0; }
	}
	
	public class Instance extends com.exa.parsing.atomic.Instance {

		public Instance() {
			super(PEOr2.this, PEOr2.this.peRoot);
		}

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
			// TODO Auto-generated method stub
			return super.nextIteration(parsing, sequence, pevs);
		}
		
		
		
	}
	
	protected List<ParsingEntity> pes;
	
	public PEOr2(List<ParsingEntity> pes, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(NULL_PE, instancePEForNotify, nextPET);
		this.pes = pes;
	}

	public PEOr2() {
		this(new ArrayList<ParsingEntity>(), null, PETransformer.petEOS());
	}

	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		DataBuffer db = parsing.bufferize();
		
		int nb = 0;
		InstanceSpecificData isdOK = null;
		for(ParsingEntity pe : pes) {
			InstanceSpecificData isd = new InstanceSpecificData(pe);
			
			ParsingEntity currentPE = isd.check(parsing);
			while(!currentPE.isFinal() && currentPE != PE_NEXT_CHECK) currentPE = isd.check(parsing);
			if(!currentPE.failed()) {
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
		
		if(isdOK.currentPE == PE_NEXT_CHECK) return nextPET.getPE().check(parsing, pevs);
		
		return nextPET.get(isdOK.currentPE, parsing, pevs);
	}
	
	public PEOr2 add(ParsingEntity pe) { pes.add(pe); return this; }
	
	public PEOr2 add(String str) { pes.add(new PEWord(str)); return this; }

}
