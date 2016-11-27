package com.exa.parsing.ebnf.predefs;


import com.exa.lexing.Language;
import com.exa.lexing.WordIterator;
import com.exa.parsing.ExpMan;
import com.exa.parsing.Parser;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ebnf.RulesConfig;
import com.exa.utils.ManagedException;

public class PreParser extends Parser<RulesConfig> {
	
	public PreParser(Language language, boolean debugOn) {
		super(language, debugOn);
	}
	
	public PreParser() { this(new PreLanguage(), false); }
	
	public PreParser(boolean debugOn) { this(new PreLanguage(), debugOn); }
	
	@Override
	public ExpMan<RulesConfig> createExpMan(WordIterator wi) throws ManagedException {
		return new PreExpMan();
	}

	@Override
	public boolean listen(ParsingEntity pe) { return true; }
	
	
	
}
