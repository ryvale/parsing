package com.exa.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exa.lexing.WordIterator;
import com.exa.utils.ManagedException;

public class Parsing<T> {
	public static final FinalPE PE_BREAK = new FinalPE();
	public static final FinalPE PE_PENDING = new FinalPE();
	
	protected WordIterator wi;
	
	protected String currentWrd;
	protected String currentBlankBefore;
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
		
		throw new ManagedException((currentWrd == null ? "" : "Error near " + currentWrd + "\n") + currentPE.asPEFail().getErrorMessage());
	}
	
	public boolean expressionIsValid() throws ManagedException {
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
	}
	
	public T getResult() { return expMan.lastResult(); }
	
	public ParsingEntity notifyEvent(List<ParsingEvent> pevs, ParsingEntity pe, int sequence2, ParsingEntity result) throws ManagedException {
		if(realResults.containsKey(sequence2)) result = realResults.get(sequence2);
		
		if(parser.notifyEvent(pe, result)) 
			pevs.add(new ParsingEvent(pe, result, currentWrd));
		
		
		return result;
	}
	
	public String getNextStringAndReturn() throws ManagedException {
		String curWrd = currentWrd;
		String curBlank = currentBlankBefore;
		
		String res = nextString();
		
		wi.addInWordBuffer(currentWrd, currentBlankBefore);
		
		currentWrd = curWrd;
		currentBlankBefore = curBlank;
		
		return res;
	}
		
	protected void endParsing()  { }
	
	public String nextString() throws ManagedException { 
		
		currentWrd = wi.nextString();
		if(debugOn) System.out.println(currentWrd);
		currentBlankBefore = wi.readBlank();
		
		return currentWrd;
	}
	
	public String currentWord() { return currentWrd; }
	
	public void setDebugOn(boolean debugOn) { this.debugOn = debugOn; }
	
	public int newParsing() { return sequence++; }
	
	public void registerResult(Integer sequence, ParsingEntity res) {
		realResults.put(sequence, res);
	}
	
	
	public boolean listens(ParsingEntity pe) { return parser.listens(pe); }

	public String readBlank() { return currentBlankBefore; }
}
