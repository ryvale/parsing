package com.exa.parsing;

public abstract class PETWithPE extends PETransformer {
	protected ParsingEntity pe;
	
	public PETWithPE(ParsingEntity pe) {
		super();
		this.pe = pe;
	}
	
	public ParsingEntity getPE() { return pe; }
	public void setPE(ParsingEntity pe) { this.pe = pe; }
}
