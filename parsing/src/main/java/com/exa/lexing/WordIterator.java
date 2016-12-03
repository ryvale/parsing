package com.exa.lexing;

import java.util.ArrayList;
import java.util.List;

import com.exa.lexing.CharReader.Buffer;
import com.exa.utils.ManagedException;

public class WordIterator implements Cloneable {
	protected CharReader charReader;
	protected LexingRules lexingRules;
	
	protected List<String> bufferStrings = new ArrayList<String>();
	
	public WordIterator(CharReader charReader, LexingRules lexingRules, List<String> bufferStrings) {
		this.charReader = charReader;
		this.lexingRules = lexingRules;
		this.bufferStrings.addAll(bufferStrings);
	}
	
	public WordIterator(CharReader charReader, LexingRules lexingRules) {
		this(charReader, lexingRules, new ArrayList<String>());
	}
	
	public WordIterator(String str, LexingRules lexingRules) throws ManagedException {
		this(new CharReader(str), lexingRules);
	}
	
	public void reset(String str) throws ManagedException {
		charReader.reset(str);
	}
	
	public String nextString() throws ManagedException {
		if(bufferStrings.size() > 0) {
			String currentWrd = bufferStrings.get(0);
			bufferStrings.remove(0);
						
			return currentWrd;
		}
		
		String res = lexingRules.nextString(charReader);
		
		return res;
	}

	public CharReader getCharReader() { return charReader; }

	public LexingRules getLexingRules() { return lexingRules; }
	
	public void close() throws ManagedException { 
		charReader.close(); 
	}
	
	public void addInWordBuffer(String word) {
		bufferStrings.add(word);
	}
	
	public void rewind(String word) { charReader.addInAnalysisBuffer(word);}
	
	@Override
	public WordIterator clone() {
		return new WordIterator(charReader.clone(), lexingRules, bufferStrings);
	}

	//public DataBuffer monitorCharReading(boolean toBeBuffered) { return charReader.monitorCharReading(toBeBuffered); }
	
	public Buffer bufferize() { return charReader.bufferize(); }
	
	public boolean hasNextString() throws ManagedException {
		Character c = lexingRules.nextNonBlankChar(charReader);
		if(c == null) return false;
		
		charReader.addInAnalysisBuffer(c);
		return true;
	}
	
	/*public void trimLeft(DataBuffer db) {
		lexingRules.trimLeft(db);
	}*/
	
	public String trimLeft(String str) {
		return lexingRules.trimLeft(str);
	}

	public String trimLeft(Buffer db) {
		return lexingRules.trimLeft(db);
	}
	
}
