package com.exa.parsing.ebnf.predefs;

import java.util.HashMap;
import java.util.Map;

import com.exa.lexing.Language;
import com.exa.lexing.WordIterator;
import com.exa.parsing.ExpMan;
import com.exa.parsing.Parser;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingRuleBuilder;
import com.exa.parsing.ebnf.RulesConfig;
import com.exa.parsing.ebnf.Standards;
import com.exa.utils.ManagedException;

public class PreParser extends Parser<RulesConfig> {
	public static final ParsingEntity PE_LX_IDENTIFIER = ParsingRuleBuilder.peOneIterationCheck(Standards.PER_LX_INDENTIFIER);
	public static final ParsingEntity PE_LX_INTEGER = ParsingRuleBuilder.peOneIterationCheck(Standards.PER_LX_INTEGER);
	
	/*public static final Map<String, ParsingEntity> PES_COMMON = new HashMap<String, ParsingEntity>();
	
	static {
		PES_COMMON.put("LX_IDENTIFIER", ParsingRuleBuilder.peOneIterationCheck(Standards.PER_LX_INDENTIFIER));
	}*/
	
	protected Map<String, ParsingEntity> standardPESToAdd = new HashMap<String, ParsingEntity>();
	
	public PreParser(Language language, boolean debugOn) {
		super(language, debugOn);
	}
	
	public PreParser addCommonParsingEntity(String name, ParsingEntity pe) { standardPESToAdd.put(name, pe); return this;}
	
	public PreParser() { this(new PreLanguage(), false); }
	
	public PreParser(boolean debugOn) { this(new PreLanguage(), debugOn); }
	
	@Override
	public ExpMan<RulesConfig> createExpMan(WordIterator wi) throws ManagedException {
		return new PreExpMan();
	}

	@Override
	public boolean listen(ParsingEntity pe) { return true; }

	@Override
	public RulesConfig parse(String str) throws ManagedException {
		RulesConfig rc = super.parse(str);
		
		for(String name : standardPESToAdd.keySet()) {
			rc.addPredefinedRule(name, standardPESToAdd.get(name));
		}
		
		return rc;
	}

	@Override
	public RulesConfig parseFile(String fileName) throws ManagedException {
		RulesConfig rc = super.parseFile(fileName);
		
		for(String name : standardPESToAdd.keySet()) {
			rc.addPredefinedRule(name, standardPESToAdd.get(name));
		}
		// TODO Auto-generated method stub
		return rc;
	}
	
	
	
}
