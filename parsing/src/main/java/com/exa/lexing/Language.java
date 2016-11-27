package com.exa.lexing;

import java.util.Set;

import com.exa.parsing.ParsingEntity;
import com.exa.utils.ManagedException;

public class Language {
	protected LexingRules lexingRules;
	protected Set<String> blankStrings;
	protected ParsingEntity peRoot;

	public Language(LexingRules lexingRules, Set<String> blankStrings, ParsingEntity peRoot) {
		this.lexingRules = lexingRules;
		this.blankStrings = blankStrings;
		this.peRoot = peRoot;
	}

	public LexingRules getLexingRules() { return lexingRules; }

	public ParsingEntity getPERoot() {	return peRoot; }
	
	public boolean knowsAsBlank(String w) {
		return blankStrings.contains(w);
	}
	
	public WordIterator newFileWordIterator(String fileName, String charset) throws ManagedException {
		EscapeCharMan ecm = new EscapeCharMan.Standard(new BufferedFileCharIterator(fileName, charset));
		return new WordIterator(new CharReader(ecm), lexingRules);
	}
	
	
	public WordIterator newStringWordIterator(String str) throws ManagedException {
		return new WordIterator(new CharReader(new StringCharIterator(str)), lexingRules);
	}
	
	
}
