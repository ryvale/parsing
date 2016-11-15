package com.exa.parsing;

public class PEFail extends ParsingEntity {
	public static final String DEFAULT_MESSAGE = "Unexpected error.";
	
	protected String errorMessage;
	
	public PEFail(String errorMessage) { 
		this.errorMessage = errorMessage; 
	}
	
	public PEFail() { this(DEFAULT_MESSAGE); }

	public String getErrorMessage() { return errorMessage; }

	@Override
	public PEFail asPEFail() { return this; }

	@Override
	public boolean isFinal() { return true; }
	
	
}
