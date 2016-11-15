package com.exa.parsing.atomic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.exa.parsing.PEFail;
import com.exa.parsing.PETWithPE;
import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class PEOr extends PEAtomic {
	
	class InstanceSpecificData {
		ParsingEntity orBranchPE;
		List<ParsingEvent> pevs;
		ParsingEntity currentPE;
		
		public InstanceSpecificData(ParsingEntity orBranchPE) {
			super();
			this.orBranchPE = orBranchPE;
			currentPE = orBranchPE;
			pevs = new ArrayList<>();
		}
		
		ParsingEntity check(Parsing<?> parsing) throws ManagedException {
			return currentPE = currentPE.check(parsing, pevs);
		}
		
		
	}
	
	class Instance extends com.exa.parsing.atomic.Instance {
		Map<ParsingEntity, InstanceSpecificData> isds;
		//List<ParsingEntity> npes;

		public Instance(Map<ParsingEntity, InstanceSpecificData> isds) {
			super(PEOr.this, PEOr.this.peRoot);
			this.isds = isds;
		}
		
		@Override
		public boolean checkFinal() {
			for(InstanceSpecificData isd : isds.values()) {
				if(isd.currentPE.checkFinal()) continue;
				
				return false;
			}
			
			return true;
		}

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			InstanceSpecificData isdOK = null;
			InstanceSpecificData isd = null;
			
			Iterator<Map.Entry<ParsingEntity, InstanceSpecificData>> it = isds.entrySet().iterator();
			
			while(it.hasNext()) {
				Map.Entry<ParsingEntity, InstanceSpecificData> entry = it.next();
				isd = entry.getValue();
				if(isd.check(parsing).failed()) {
					it.remove();
					continue;
				}
				isdOK = isd;
			}
			
			if(isds.size() == 0) return isd.currentPE;
			
			if(isds.size() == 1) {
				pevs.addAll(isdOK.pevs);
				if(isdOK.currentPE.isFinal()) {
					return nextPET.get(isdOK.currentPE, parsing, pevs);
				}
				return new EndInstance(isdOK.currentPE);
			}
			
			return PE_INSTANCE;
		}
		
	}
	
	class EndInstance extends com.exa.parsing.atomic.Instance {
		
		public EndInstance(ParsingEntity peRoot) {
			super(PEOr.this, peRoot);
		}

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			super.nextIteration(parsing, sequence, pevs);
			
			if(currentPE.failed()) return currentPE;
			
			if(currentPE.isFinal()) return PEOr.this.nextPET.get(currentPE, parsing, pevs);
			
			return PE_INSTANCE;
		}
		
		
	}
	
	protected List<ParsingEntity> pes;

	public PEOr(List<ParsingEntity> pes, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(NULL_PE, instancePEForNotify, nextPET);
		this.pes = pes;
	}
	
	public PEOr() {
		this(new ArrayList<ParsingEntity>(), null, PETransformer.petEOS());
	}
	

	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		Map<ParsingEntity, InstanceSpecificData> isds = new HashMap<ParsingEntity, PEOr.InstanceSpecificData>();
		 
		//ParsingEntity peOK = null; //ParsingEntity currentPE = new PEFail("No parsing entity encountered while expecting in a or");
		InstanceSpecificData isd = null; InstanceSpecificData isdOK = null;
		
		for(ParsingEntity pe : pes) {
			isd = new InstanceSpecificData(pe);
			
			if(isd.check(parsing).failed()) continue;
			
			isds.put(pe, isd);
			isdOK = isd;
			
		}
		
		if(isds.size() == 0) return new PEFail("No parsing entity encountered while expecting in a or");
		
		if(isds.size() == 1) {
			pevs.addAll(isdOK.pevs);
			if(isdOK.currentPE.isFinal()) {
				return nextPET.get(isdOK.currentPE, parsing, pevs);
			}
			return new EndInstance(isdOK.currentPE);
		}
		
		return new Instance(isds);
	}
	
	public PEOr add(ParsingEntity pe) { pes.add(pe); return this; }

	@Override
	public boolean checkFinal() {
		for(ParsingEntity pe : pes) {
			if(pe.checkFinal()) continue;
			
			return false;
		}
		
		return true;
	}
	
	

}
