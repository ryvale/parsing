package com.exa.parsing;

import java.util.List;

import com.exa.utils.ManagedException;

public class ExpMan<T> {
	protected Parsing<T> parsing;
	
	public ExpMan(Parsing<T> parsing) {
		super();
		this.parsing = parsing;
	}

	public ParsingEntity push(ParsingEntity currentPE, List<ParsingEvent> peEvents) throws ManagedException {
		return currentPE;
	}
	
	public T reset() { return null; }
	
	//public T initialize() { return null; }
	
	public T compute() throws ManagedException { return null; }
	
	public T lastResult() { return null; }

	public void setParsing(Parsing<T> parsing) { this.parsing = parsing; }
	
}
