package com.exa.parsing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.exa.lexing.CharReader.Buffer;
import com.exa.utils.ManagedException;

public class PEUnordered extends ParsingEntity {
	protected static final ParsingEntity PE_BRANCH_FAIL = new PEFail();
	
	class PESpecic {
		ParsingEntity orBranchPE;
		ParsingEntity convertedBranchPE;
		
		public PESpecic(ParsingEntity orBranchPE, boolean optional) {
			this.orBranchPE = orBranchPE;
			this.convertedBranchPE = optional ? new OptionPE(orBranchPE) : orBranchPE;
		}
		
		boolean isOptional() { return orBranchPE != convertedBranchPE; }
		
	}
	
	class InstanceSpecificData {
		ParsingEntity orBranchPE;
		ParsingEntity convertedBranchPE;
		List<ParsingEvent> pevs;
		ParsingEntity currentPE;
		String str = null;
		
		public InstanceSpecificData(PESpecic pesp) {
			super();
			this.orBranchPE = pesp.orBranchPE;
			this.convertedBranchPE = pesp.convertedBranchPE;
			currentPE = convertedBranchPE;
			pevs = new ArrayList<>();
		}
		
		ParsingEntity check(Parsing<?> parsing) throws ManagedException {
			return currentPE = currentPE.checkBranch(parsing, pevs);
		}
		
		public void reset() {
			pevs.clear();
			currentPE = convertedBranchPE;
		}
		
		boolean isOptional() { return orBranchPE != convertedBranchPE; }
		
	}

	class OptionPE extends ParsingEntity {
		protected ParsingEntity peRoot;

		public OptionPE(ParsingEntity peRoot) {
			this.peRoot = peRoot;
		}

		@Override
		public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
			ParsingEntity currentPE = peRoot.checkBranch(parsing, pevs);
			if(currentPE.failed()) return PE_BRANCH_FAIL;
			
			return currentPE;
		}
		
		
	}
	
	class Instance extends ParsingEntity {
		
	}
	
	protected List<PESpecic> pes = new ArrayList<>();
	
	public PEUnordered(PETWithPE nextPET) {
		super(nextPET);
	}
	
	public PEUnordered() {
		this(PETransformer.petEOS());
	}
	
	public PEUnordered add(ParsingEntity pe, boolean optional) {
		pes.add(new PESpecic(pe, optional));
		return this;
	}
	
	public PEUnordered add(ParsingEntity pe) {
		return add(pe, true);
	}
	
	public PEUnordered add(String word) {
		return add(new PEWord(word), true);
	}
	
	public PEUnordered add(String word, boolean optional) {
		return add(new PEWord(word), optional);
	}
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs)	throws ManagedException {
		if(!parsing.hasNextString()) {
			for(PESpecic pesp : pes) {
				if(!pesp.isOptional()) return EOS_FAIL;
			}
			
			return EOS;
		}
		
		Buffer dbOrigine = parsing.bufferize();
		
		Buffer db = parsing.bufferize();
		
		List<InstanceSpecificData> npes = new ArrayList<>(), pesOK = new ArrayList<>();
		for(PESpecic pesp : pes) npes.add(new InstanceSpecificData(pesp));
		
		while(npes.size()> 0) {
			int nb = npes.size();
			Iterator<InstanceSpecificData> it = npes.iterator();
			
			while(it.hasNext()) {
				InstanceSpecificData isd = it.next(); isd.reset();
				
				ParsingEntity currentPE = isd.check(parsing);
				
				if(currentPE.asPEFail() == null) {
					db.markPosition();
					pesOK.add(isd);
					it.remove();
				}
				else db.rewind();
			}
			
			if(nb == npes.size()) break;
		}
		
		for(InstanceSpecificData isd : npes) {
			if(isd.currentPE == PE_BRANCH_FAIL) continue;
			
			dbOrigine.rewindAndRelease();
			return notifyResult(parsing, DEFAULT_FAIL, pevs);
		}
		
		if(pesOK.size() == 0) {
			dbOrigine.rewindAndRelease();
			return notifyResult(parsing, nextPET.get(OK, parsing, pevs), pevs);
		}
		
		List<ParsingEvent> lpes = new ArrayList<>();
		for(InstanceSpecificData isd : pesOK) {
			lpes.addAll(isd.pevs);
		}
		
		return notifyResult(parsing, nextPET.get(OK, parsing, pevs), lpes, pevs);
	}

	@Override
	public boolean checkFinal() {
		for(PESpecic pesp : pes) {
			if(pesp.isOptional()) continue;
			return false;
		}
		return true;
	}
	
	
	
}
