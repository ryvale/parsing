package com.exa.parsing;

public class ParsingRuleBuilder {
	static class BlankPE extends ParsingEntity {}
	
	protected ParsingEntity peRoot, currentPE;
	
	public ParsingRuleBuilder(ParsingEntity peRoot) {
		this.peRoot = peRoot;
		this.currentPE = peRoot;
	}
	
	public ParsingRuleBuilder() { this(new BlankPE()); }
	
	public ParsingRuleBuilder(String wrd) {
		this(new PEWord(wrd)); 
	}
	
	public ParsingRuleBuilder(String ... wrds) {
		this(new PEWord(wrds)); 
	}
	
	public ParsingRuleBuilder next(ParsingEntity pe) {
		 currentPE.setNextPE(pe);
		 
		 currentPE = pe;
		
		return this;
	}
	
	public ParsingRuleBuilder next(String str) {
		return next(new PEWord(str));
	}
	
	public ParsingEntity parsingEntity() {
		while(peRoot != null) {
			if(peRoot instanceof BlankPE) {
				peRoot = peRoot.getNextPE();
				continue;
			}
			
			break;
		}
		return peRoot;
	}
	
	public static ParsingEntity peOneIterationCheck(PERule perOK, PETransformer petFail) {
		return new PELexerString(perOK, petFail);
	}
	
	public static ParsingEntity peOneIterationCheck(PERule perOK) {
		return new PELexerString(perOK);
	}

}
