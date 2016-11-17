package com.exa.parsing.ebnf;

import java.util.HashSet;

import com.exa.lexing.LexingRules;
import com.exa.parsing.PERule;
import com.exa.parsing.PEWord;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingRuleBuilder;
import com.exa.parsing.atomic.PEStandardAtomic;
import com.exa.parsing.atomic.PEOptional;
import com.exa.parsing.atomic.PEOr;
import com.exa.parsing.atomic.PERepeat;

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
		
	static void addOperandEx(ParsingRuleBuilder prb) {
		prb.
			next(new PEOptional(new PEWord("*", "+", "?")));
	}
	
	static void addEBNFExp(ParsingRuleBuilder prb) {
		
	}

	public RuleLanguage() {
		super(new LexingRules(), new HashSet<String>(), null);
		
		lexingRules.addActiveWord(new StringDelimiter(lexingRules, '\''));
		//lexingRules.addWordSeparator(" ");
		
		lexingRules.addWordSeparator("|");
		lexingRules.addWordSeparator("*");
		lexingRules.addWordSeparator("+");
		lexingRules.addWordSeparator("?");
		lexingRules.addWordSeparator("$");
		lexingRules.addWordSeparator("!");
		lexingRules.addWordSeparator("(");
		lexingRules.addWordSeparator(")");
		
		lexingRules.addWordSeparator("=");
		lexingRules.addWordSeparator("$=");
		lexingRules.addWordSeparator("[]=");
		
		PEOptional peOperandEx = new PEOptional(new PEWord("!"));
		PEOr pePrimitiveOperand = pePrimitiveOperand();
		
		peOperandEx.setNextPE(pePrimitiveOperand);
		
		pePrimitiveOperand.setNextPE(new PEOptional(new PEWord("*", "+", "?")));
		
		PEStandardAtomic peBinaryPartNext = new PEStandardAtomic(peOperandEx);
		
		ParsingRuleBuilder prbBinaryPart = new ParsingRuleBuilder(new PEWord("|", "=", "$=", "[]=")).
			next(peBinaryPartNext);
	
		peBinaryPartNext.setNextPE(new PERepeat(new PEStandardAtomic(prbBinaryPart.parsingEntity())));
		
		ParsingRuleBuilder prbMainRow = new ParsingRuleBuilder(new PEStandardAtomic(peOperandEx));
		
		prbMainRow.next(new PERepeat(new PEStandardAtomic(prbBinaryPart.parsingEntity())));
		
		ParsingEntity peExp = new PERepeat(prbMainRow.parsingEntity());
		
		pePrimitiveOperand.add(
			new ParsingRuleBuilder("(").
				next(new PEStandardAtomic(peExp)).
				next(")").
			parsingEntity()
		);
		
		peRoot = peExp;
	}

}
