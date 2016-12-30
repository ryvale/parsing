package com.exa.lexing;

import java.util.ArrayList;
import java.util.List;

import com.exa.buffer.CharReader;
import com.exa.buffer.CharReader.ClientBuffer;
//import com.exa.lexing.CharReader.Buffer;
import com.exa.utils.ManagedException;

public class WordIterator implements Cloneable {
	public abstract class TokenIterator {
		abstract String nextString() throws ManagedException;
	}
	
	class CharIterator extends TokenIterator {

		@Override
		String nextString() throws ManagedException {
			
			if(bufferStrings.size() > 0) {
				
				String currentWrd = bufferStrings.get(0);
				bufferStrings.remove(0);
				
				if(currentWrd.length() == 1) return currentWrd;
				
				bufferStrings.add(0, currentWrd.substring(1));
				
				return currentWrd.substring(0, 1);
			}
			
			Character c = charReader.nextChar();
			
			return c == null ? null : c.toString();
		}
		
	}
	
	class StringIterator extends TokenIterator {

		@Override
		String nextString() throws ManagedException {
			if(bufferStrings.size() > 0) {
				String currentWrd = bufferStrings.get(0);
				bufferStrings.remove(0);
							
				return currentWrd;
			}
			
			String res = lexingRules.nextString(charReader);
			
			return res;
		}
		
	}
	
	protected CharReader charReader;
	protected LexingRules lexingRules;
	protected TokenIterator tokenIterator;
	
	private StringIterator stringIterator;
	private CharIterator charIterator;
	
	protected List<String> bufferStrings = new ArrayList<String>();
	
	public WordIterator(CharReader charReader, LexingRules lexingRules, List<String> bufferStrings) {
		this.charReader = charReader;
		this.lexingRules = lexingRules;
		this.bufferStrings.addAll(bufferStrings);
		
		stringIterator = new StringIterator();
		charIterator = new CharIterator();
		
		tokenIterator = stringIterator;
		
	}
	
	public WordIterator(CharReader charReader, LexingRules lexingRules) {
		this(charReader, lexingRules, new ArrayList<String>());
	}
	
	public WordIterator(String str, LexingRules lexingRules) throws ManagedException {
		this(new CharReader(str), lexingRules);
	}
	
	public String nextString() throws ManagedException {
		return tokenIterator.nextString();
	}
	
	public Character nextChar() throws ManagedException { return charReader.nextChar(); }

	public CharReader getCharReader() { return charReader; }

	public LexingRules getLexingRules() { return lexingRules; }
	
	public void close()  { charReader.close(); }
	
	public void addInWordBuffer(String word) {
		bufferStrings.add(word);
	}
	
	public void setCharIteratorMode(boolean on) { tokenIterator = on ? charIterator : stringIterator; }
	
	public boolean getCharIteraorMode() { return tokenIterator == charIterator; }
	
	public void rewind(String word) throws ManagedException { charReader.back(word.toString());}
	
	@Override
	public WordIterator clone() {
		return new WordIterator(charReader.clone(), lexingRules, bufferStrings);
	}
	
	public ClientBuffer listen() { return charReader.listen(); }
	
	public boolean hasNextString() throws ManagedException {
		Character c = lexingRules.nextNonBlankChar(charReader);
		if(c == null) return false;
		
		charReader.back(c.toString());
		return true;
	}
	
	
	public String trimLeft(String str) {
		return lexingRules.trimLeft(str);
	}

	public String trimLeft(ClientBuffer db) {
		return lexingRules.trimLeft(db);
	}
	
}
