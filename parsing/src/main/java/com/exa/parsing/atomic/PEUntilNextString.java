package com.exa.parsing.atomic;

import java.util.List;

import com.exa.parsing.PEFail;
import com.exa.parsing.PETWithPE;
import com.exa.parsing.PETransformer;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingEvent;
import com.exa.utils.ManagedException;

public class PEUntilNextString extends PEAtomic {
	
	public static abstract class Predicat {
		public abstract boolean check(String str);
	}
	
	class Instance extends com.exa.parsing.atomic.Instance {

		public Instance() {
			super(PEUntilNextString.this, NULL_PE);
		}

		@Override
		public ParsingEntity nextIteration(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
			String ns = parsing.getNextStringAndReturn();
			
			if(ns == null) {
				if(mandatory) return new PEFail("Unexpected end of file.");
				return PEUntilNextString.this.nextPET.get(ParsingEntity.OK, parsing, pevs);
			}
			
			if(nextStringPredicat.check(ns)) return PEUntilNextString.this.nextPET.get(ParsingEntity.OK, parsing, pevs);
			
			return this;
		}
	}
	
	protected Predicat nextStringPredicat;
	protected boolean mandatory;

	public PEUntilNextString(Predicat nextStringPredicat, boolean mandatory, ParsingEntity instancePEForNotify, PETWithPE nextPET) {
		super(NULL_PE, instancePEForNotify, nextPET);
		this.nextStringPredicat = nextStringPredicat;
		this.mandatory = mandatory;
	}
	
	public PEUntilNextString(Predicat nextStringPredicat, boolean mandatory) { this(nextStringPredicat, mandatory, null, PETransformer.petEOS()); }
	
	public PEUntilNextString(final String str, boolean mandatory) {
		this(new Predicat() {
		
			@Override
			public boolean check(String currentStr) {
				
				return currentStr.equals(str) ? true : false;
			}
		}, mandatory, null, PETransformer.petEOS()); 
	}
	
	public PEUntilNextString(final String str) {
		this(new Predicat() {
		
			@Override
			public boolean check(String currentStr) {
				
				return currentStr.equals(str) ? true : false;
			}
		}, true, null, PETransformer.petEOS()); 
	}

	@Override
	protected ParsingEntity getFirstInstance(Parsing<?> parsing, int sequence, List<ParsingEvent> pevs) throws ManagedException {
		return new Instance().checkResult(parsing, sequence, pevs);
	}


}
