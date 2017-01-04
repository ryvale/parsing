package com.exa.lexing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import com.exa.buffer.CharReader;
import com.exa.chars.EscapeCharMan;
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
	
	public WordIterator newFileWordIterator(String fileName, Charset charset, boolean autoDetectCharset, EscapeCharMan escapeCharMan) throws ManagedException {
		CharReader cr;
		try {
			cr = new CharReader(new RandomAccessFile(fileName, "r"), charset, autoDetectCharset, escapeCharMan);
		} catch (IOException e) {
			throw new ManagedException(e);
		}
		return new WordIterator(cr, lexingRules);
	}
	
	public WordIterator newFileWordIterator(String fileName, Charset charset, boolean autoDetectCharset) throws ManagedException {
		return newFileWordIterator(fileName, charset, autoDetectCharset, EscapeCharMan.STANDARD);
	}
	
	public WordIterator newFileWordIterator(String fileName, Charset charset) throws ManagedException {
		return newFileWordIterator(fileName, charset, false, EscapeCharMan.STANDARD);
	}
	
	public WordIterator newFileWordIterator(String fileName) throws ManagedException {
		return newFileWordIterator(fileName, StandardCharsets.UTF_8, false, EscapeCharMan.STANDARD);
	}
	
	public WordIterator newStringWordIterator(String str) throws ManagedException {
		return new WordIterator(new CharReader(str), lexingRules);
	}

	public Set<String> getBlankStrings() {
		return blankStrings;
	}

	public void setBlankStrings(Set<String> blankStrings) {
		this.blankStrings = blankStrings;
	}

	public void setLexingRules(LexingRules lexingRules) {
		this.lexingRules = lexingRules;
	}
	
}
