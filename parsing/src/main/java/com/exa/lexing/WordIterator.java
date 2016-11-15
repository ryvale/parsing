package com.exa.lexing;

import java.util.ArrayList;
import java.util.List;

import com.exa.utils.ManagedException;

public class WordIterator implements Cloneable {
	protected CharReader charReader;
	protected LexingRules lexingRules;
	protected String readBlank = "";
	
	protected List<String> bufferStrings = new ArrayList<String>();
	protected List<String> blankStrings = new ArrayList<String>();
	
	public WordIterator(CharReader charReader, LexingRules lexingRules, List<String> bufferStrings) {
		this.charReader = charReader;
		this.lexingRules = lexingRules;
		this.bufferStrings.addAll(bufferStrings);
	}
	
	public WordIterator(CharReader charReader, LexingRules lexingRules) {
		this(charReader, lexingRules, new ArrayList<String>());
		/*super();
		this.charReader = charReader;
		this.lexingRules = lexingRules;*/
	}
	
	public WordIterator(String str, LexingRules lexingRules) throws ManagedException {
		this(new CharReader(str), lexingRules);
		/*super();
		this.charReader = new CharReader(str);
		this.lexingRules = lexingRules;*/
	}
	
	public void reset(String str) throws ManagedException {
		charReader.reset(str);
	}
	
	public String nextString() throws ManagedException {
		if(bufferStrings.size() > 0) {
			String currentWrd = bufferStrings.get(0);
			bufferStrings.remove(0);
			
			readBlank = blankStrings.get(0);
			blankStrings.remove(0);
			
			return currentWrd;
		}
		
		String res = lexingRules.nextString(charReader);
		
		readBlank = lexingRules.readBlank();
		return res;
	}

	public CharReader getCharReader() { return charReader; }

	public LexingRules getLexingRules() { return lexingRules; }
	
	public void close() throws ManagedException { 
		charReader.close(); 
	}
	
	public void addInWordBuffer(String word, String blank) {
		bufferStrings.add(word);
		blankStrings.add(blank);
	}
	
	public String readBlank() { return readBlank; }
	
	@Override
	public WordIterator clone() throws CloneNotSupportedException {
		return new WordIterator(charReader.clone(), lexingRules, bufferStrings);
	}

	
}
