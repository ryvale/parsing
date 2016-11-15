package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.PETWithPE;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public abstract class PEAtomic extends ParsingEntity {
	public final static ParsingEntity PE_INSTANCE = new ParsingEntity();
	public final static ParsingEntity PE_NEXT_CHECK = new ParsingEntity();
	
	public final static ParsingEntity NULL_PE = new ParsingEntity() {

		@Override
		public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			return this;
		}
		
	};
	
	protected ParsingEntity peRoot;
	protected ParsingEntity instancePEForNotify;
	
	public PEAtomic(ParsingEntity peRoot, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(nextPET);
		this.peRoot = peRoot;
		peRoot.setRoot(false);
		
		this.instancePEForNotify = instancePEForNotify == null ? this : instancePEForNotify;
	}
	
	@Override
	public ParsingEntity checkResult(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		return getFirstInstance(parsing, sequence, pevs);
	}
	
	public ParsingEntity getInstancePEForNotify() {	return instancePEForNotify;	}
	
	//in override use checkResult
	protected abstract ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException;
	
}
