package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public abstract class PETransformer {
	public final static PETransformer CURRENT = new PETransformer() {
		
		@Override
		public ParsingEntity get(ParsingEntity currentPE, Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException {
			return currentPE;
		}
	};
	/*public static final PETWithPE OK = new PETIdentity(ParsingEntity.OK);
	public static final PETWithPE EOS = new PETIdentity(ParsingEntity.EOS);
	public static final PETWithPE FAIL = new PETIdentity(ParsingEntity.DEFAULT_FAIL);*/
	public static PETWithPE petOK() { return new PETIdentity(ParsingEntity.OK);}
	public static PETIdentity petEOS() { return new PETIdentity(ParsingEntity.EOS);}
	public static PETWithPE petFAIL() { return new PETIdentity(ParsingEntity.DEFAULT_FAIL);}
	
	public abstract ParsingEntity get(ParsingEntity currentPE, Parsing<?> parsing, List<ParsingEvent> pevs) throws ManagedException;
}
