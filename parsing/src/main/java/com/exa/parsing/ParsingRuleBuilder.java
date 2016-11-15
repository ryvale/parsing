package com.exa.parsing;

import com.exa.parsing.atomic.PEStandardRoutable;

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
		return new PEStandardRoutable().
				addRoute(PERule.ANY_CASE, petFail).
				addPrimaryRouteForNext(perOK);
	}
	
	public static ParsingEntity peOneIterationCheck(PERule perOK) {
		return new PEStandardRoutable().
				addRoute(PERule.ANY_CASE, PETransformer.petFAIL()).
				addPrimaryRouteForNext(perOK);
	}
	
	public static ParsingEntity peExitOnFinal(ParsingEntity peRoot) {
		return new PEStandardRoutable(peRoot).
				addRoute(PERule.FAIL, PETransformer.CURRENT).
				addPrimaryRouteForNext(PERule.NON_FAIL_FINAL);
	}

}
