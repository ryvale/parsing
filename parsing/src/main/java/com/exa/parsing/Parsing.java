package com.exa.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exa.lexing.CharReader.DataBuffer;
import com.exa.lexing.WordIterator;
import com.exa.utils.ManagedException;

public class Parsing<T> {
	public static final FinalPE PE_BREAK = new FinalPE();
	public static final FinalPE PE_PENDING = new FinalPE();
	
	protected WordIterator wi;
	
	protected String lexerWord;
	protected String lexerBlankBefore;
	//protected String currentWord = null;
	//protected StringBuilder sbCurrentWord = new StringBuilder();
	//protected DataBuffer dataBuffer;
	//protected String blankBefore = null;
	protected ParsingEntity currentPE;
	protected Parser<T> parser;
	protected Parsing<T> parent;
	protected ExpMan<T> expMan;
	protected List<ParsingEvent> peEvents = new ArrayList<>();
	protected Map<Integer, ParsingEntity> realResults = new HashMap<>();
	
	public boolean debugOn;
	
	protected int sequence = 0;
	
	public Parsing(Parser<T> parser, WordIterator wi, ExpMan<T> expMan, Parsing<T> parent, boolean debugOn) {
		this.wi = wi;
		this.parser = parser;
		this.expMan = expMan;
		
		this.parent = parent;
		this.debugOn = debugOn;
	}
		
	public T compute() throws ManagedException { 
		if(expressionIsValid()) return expMan.compute();
		
		throw new ManagedException((lexerWord == null ? "" : "Error near " + lexerWord + "\n") + currentPE.asPEFail().getErrorMessage());
	}
	
	/*public boolean expressionIsValid() throws ManagedException {
		expMan.setParsing(this);
		expMan.reset();
		
		currentPE = parser.getLanguage().getPERoot();
		while(nextString() != null) {
			peEvents.clear();
			realResults.clear();
			currentPE = currentPE.check(this, peEvents);
			
			if(currentPE == PE_BREAK) break;
			
			if(currentPE instanceof PEFail) return false;
			
			currentPE = expMan.push(currentWrd, currentPE, peEvents);
			
			if(currentPE instanceof PEFail) return false;
		}
		
		if(currentPE.checkFinal()) return true;
	
		currentPE = new PEFail("Unexpected end of file.");
		
		return false;
	}*/
	
	public boolean expressionIsValid() throws ManagedException {
		//dataBuffer = wi.bufferize();
		expMan.setParsing(this);
		expMan.reset();
		
		currentPE = parser.getLanguage().getPERoot();
		while(hasNextString()) {
			nextCheck();
			if(currentPE.failed()) return false;
			
			//currentPE = expMan.push(lexerWord, currentPE, peEvents);
			
			if(currentPE instanceof PEFail) return false;
			if(currentPE.isFinal()) break;
		}
		
		//dataBuffer.release();
		
		if(currentPE.failed()) return false;
		
		if(hasNextString()) {
			currentPE = new PEFail("Remain string '" + nextString() + "' after parsing." );
			return false;
		}
			
		if(currentPE.checkFinal()) return true;
		
		currentPE = new PEFail("Unexpected end of file" );
		return false;
	}
	
	
	
	public T getResult() { return expMan.lastResult(); }
	
	public ParsingEntity notifyEvent(List<ParsingEvent> pevs, ParsingEntity pe, int sequence2, ParsingEntity result) throws ManagedException {
		if(realResults.containsKey(sequence2)) result = realResults.get(sequence2);
		
		if(parser.notifyEvent(pe, result)) 
			pevs.add(new ParsingEvent(pe, result, this));
		
		
		return result;
	}
	
	public String getNextStringAndReturn() throws ManagedException {
		String curWrd = lexerWord;
		String curBlank = lexerBlankBefore;
		
		String res = nextString();
		
		wi.addInWordBuffer(lexerWord);
		
		lexerWord = curWrd;
		lexerBlankBefore = curBlank;
		
		return res;
	}
		
	protected void endParsing()  { }
	
	public String nextString() throws ManagedException { 
		lexerWord = wi.nextString();
		if(debugOn) System.out.println(lexerWord);
		
		return lexerWord;
	}
	
	public ParsingEntity nextCheck() throws ManagedException {
		peEvents.clear();
		realResults.clear();
		
		currentPE = currentPE.check(this, peEvents);
		
		return currentPE;
	}
	
	public String lexerWord() { return lexerWord; }
	
	public void setDebugOn(boolean debugOn) { this.debugOn = debugOn; }
	
	public int newParsing() { return sequence++; }
	
	public void registerResult(Integer sequence, ParsingEntity res) {
		realResults.put(sequence, res);
	}
	
	public boolean listens(ParsingEntity pe) { return parser.listens(pe); }

	public String lexerBlankBefore() { return lexerBlankBefore; }
	
	//public String currentWord() { return currentWord; }
	
	//public String readingWord() { return dataBuffer.value(); }
	
	public void rewindWord(String word) {
		wi.rewind(word);
	}
	
	public boolean hasNextString() throws ManagedException {
		return wi.hasNextString();
	}
	
	public DataBuffer firstBufferizeRead() throws ManagedException {
		DataBuffer db = bufferize();
		if(nextString() == null) { db.release(); return null; }
		trimLeft(db);
		
		return db;
	}
	
	public DataBuffer bufferize() { return wi.bufferize(); }
	
	public void trimLeft(DataBuffer db) { wi.trimLeft(db); }
	
}
