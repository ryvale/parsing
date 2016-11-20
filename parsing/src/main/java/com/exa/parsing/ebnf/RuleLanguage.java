package com.exa.parsing.ebnf;

import java.util.HashSet;

import com.exa.lexing.LexingRules;
import com.exa.parsing.PERule;
import com.exa.parsing.PEWord;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingRuleBuilder;
import com.exa.parsing.PEAtomic;
import com.exa.parsing.PEOptional;
import com.exa.parsing.PEOr;
import com.exa.parsing.PERepeat;


public class RuleLanguage extends com.exa.lexing.Language {	
	
	static PEOr pePrimitiveOperand() {
		PEOr res = new PEOr().add(
		
			ParsingRuleBuilder.peOneIterationCheck(new PERule() {
				
				@Override
				public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
					String currentWord = parsing.lexerWord().trim();
					if(currentWord.charAt(0) != '\'' || currentWord.charAt(currentWord.length() - 1) != '\'') return false;
					
					currentWord = currentWord.substring(1, currentWord.length() -1);
	
					int p = currentWord.indexOf("'");
					
					if(p == -1) return true;
					
					if(currentWord.charAt(p-1) != '\\') return false;
					
					return true;
				}
			})
		
		).add(
				ParsingRuleBuilder.peOneIterationCheck(new PERule() {
					
					@Override
					public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
						return Standards.isIdentifier(parsing.lexerWord());
					}
				})
		).add(
				new PEWord("$")
		);
		
		return res;
	}
	
	static void addEBNFExp(ParsingRuleBuilder prb) {}

	public RuleLanguage() {
		super(new LexingRules(), new HashSet<String>(), null);
		
		lexingRules.addActiveWord(new StringDelimiter(lexingRules, '\''));
		
		lexingRules.addWordSeparator("|", "*", "+", "?", "$", "!", "(", ")", "=", "$=", "[]=");
		
		PEOr pePrimitiveOperand = pePrimitiveOperand();
		pePrimitiveOperand.setNextPE(new PEOptional("*", "+", "?"));
		
		PEOptional peOperandEx = new PEOptional("!");
		peOperandEx.setNextPE(pePrimitiveOperand);
		
		PEAtomic peBinaryPartNext = new PEAtomic(peOperandEx);
		
		ParsingRuleBuilder prbBinaryPart = new ParsingRuleBuilder("|", "=", "$=", "[]=").
			next(peBinaryPartNext);
	
		peBinaryPartNext.setNextPE(new PERepeat(prbBinaryPart.parsingEntity()));
		
		ParsingRuleBuilder prbMainRow = new ParsingRuleBuilder(new PEAtomic(peOperandEx));
		
		prbMainRow.next(new PERepeat(prbBinaryPart.parsingEntity()));
		
		ParsingEntity peExp = new PERepeat(prbMainRow.parsingEntity());
		
		pePrimitiveOperand.add(
			new ParsingRuleBuilder("(").
				next(new PEAtomic(peExp)).
				next(")").
			parsingEntity()
		);
		
		peRoot = peExp;
	}

}
