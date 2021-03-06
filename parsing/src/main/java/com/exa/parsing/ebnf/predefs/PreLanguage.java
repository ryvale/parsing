package com.exa.parsing.ebnf.predefs;

import java.util.HashSet;

import com.exa.lexing.Language;
import com.exa.lexing.LexingRules;
import com.exa.parsing.PERepeat;
import com.exa.parsing.PERule;
import com.exa.parsing.PEUnordered;
import com.exa.parsing.PEUntilNextString;
import com.exa.parsing.PEWord;
import com.exa.parsing.Parsing;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingRuleBuilder;
import com.exa.parsing.ebnf.StringDelimiter;
import com.exa.stdxpression.parsing.NumberAW;
import com.exa.utils.ManagedException;

import static com.exa.parsing.ebnf.Standards.IDENTIFIER_CHARS;

public class PreLanguage extends Language {
	public static final String WS_RULE_PART_SEP = ":";
	public static final String WS_RULE_SEP = ";";
	
	public final static ParsingEntity PE_RULE_NAME;
	public final static ParsingEntity PE_RULE_EXP;
		
	public final static PERule PER_RULE_NAME = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) throws ManagedException {
			
			String id = parsing.lexerWord();
			
			if(id.length() == 0) return false; 
			
			char firstChar = id.charAt(0);
			
			if(NumberAW.NUMBER_CHARS.indexOf(firstChar)>=0)  return false; 
			
			if(IDENTIFIER_CHARS.indexOf(firstChar)<0)  return false; 
			
			for(int i=1; i<id.length(); i++) {
				if(IDENTIFIER_CHARS.indexOf(id.charAt(i))<0)  return false; 
			}
			
			return true;
		}
	};
	
	static {
		PE_RULE_NAME = ParsingRuleBuilder.peOneIterationCheck(PER_RULE_NAME);
		PE_RULE_EXP = new PEUntilNextString(WS_RULE_SEP, true, false);
	}
	
	ParsingEntity peStringLiteral() {
		return ParsingRuleBuilder.peOneIterationCheck(new PERule() {
			
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
		});
	}
	
	public PreLanguage() { 
		super(new LexingRules(), new HashSet<String>(), null);
		lexingRules.addWordSeparator(WS_RULE_PART_SEP);
		lexingRules.addWordSeparator(WS_RULE_SEP);
		
		lexingRules.addActiveWord(new StringDelimiter(lexingRules, '\''));
		
		//lexingRules.addActiveWord(new WordWithOpenCloseDelimiter(lexingRules, WS_KEYWORD_DELIMITER.charAt(0), WS_KEYWORD_DELIMITER.charAt(0)));
		
		ParsingEntity pe0 = new PEWord("ignore");
		pe0.setNextPE(peStringLiteral()).setNextPE(WS_RULE_SEP);
		
		ParsingEntity pe1 = 
			new ParsingRuleBuilder("separators").
				next(peStringLiteral()).
				optional(new PERepeat(
							new ParsingRuleBuilder(",").
								next(peStringLiteral()).
							parsingEntity()
						)
				).
				next(WS_RULE_SEP).
			parsingEntity();
		
		ParsingEntity peRow = new 
				ParsingRuleBuilder(PE_RULE_NAME).
				next(WS_RULE_PART_SEP).
				next(PE_RULE_EXP).
				next(WS_RULE_SEP).
			parsingEntity();
		
		
		peRoot = new PEUnordered().add(pe0).add(pe1);
		peRoot.setNextPE(new PERepeat(peRow));
	}
}
